package ru.otus.hw.config;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.model.mongo.MongoBook;
import ru.otus.hw.model.postgres.Book;
import ru.otus.hw.service.BatchLinkService;
import ru.otus.hw.service.cleanup.CleanUpBookService;
import ru.otus.hw.service.mongo.MongoAuthorService;
import ru.otus.hw.service.mongo.MongoBookService;
import ru.otus.hw.service.mongo.MongoGenreService;
import ru.otus.hw.service.processor.BookProcessor;

import java.util.List;

@SuppressWarnings("unused")
@RequiredArgsConstructor
@Configuration
public class JobBookConfig {

    public static final String IMPORT_BOOK_JOB_NAME = "importBookJob";

    private static final int  CHUNK_SIZE = 5;

    private final Logger logger = LoggerFactory.getLogger("Batch");

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final EntityManagerFactory postgresqlEntityManagerFactory;

    private final MongoBookService mongoBookService;

    private final MongoAuthorService mongoAuthorService;

    private final MongoGenreService mongoGenreService;

    private final BatchLinkService batchLinkService;

    private final CleanUpBookService cleanUpGenreService;

    @StepScope()
    @Bean
    public JpaPagingItemReader<Book> bookItemReader(
            @Value("#{jobParameters['currentItemCount']}") String currentItemCount) {
        return new JpaPagingItemReaderBuilder<Book>()
                .name("bookItemReader")
                .entityManagerFactory(postgresqlEntityManagerFactory)
                .queryString("SELECT b FROM Book b WHERE b.imported = false")
                .currentItemCount(Integer.parseInt(currentItemCount))
                .pageSize(CHUNK_SIZE)
                .build();
    }


    @StepScope()
    @Bean
    public BookProcessor bookItemProcessor() {
        return new BookProcessor(batchLinkService, mongoBookService);
    }

    @StepScope()
    @Bean
    public MongoItemWriter<MongoBook> bookItemWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<MongoBook>()
                .template(mongoTemplate)
                .collection("books")
                .build();
    }

    @Bean
    public Step transformBookStep(
            JpaPagingItemReader<Book> reader,
            MongoItemWriter<MongoBook> writer,
            ItemProcessor<Book, MongoBook> processor) {
        return new StepBuilder("transformBookStep", jobRepository)
                .<Book, MongoBook>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(new ItemReadListener<>() {
                    public void beforeRead() {
                        logger.info("Начало чтения");
                    }

                    public void afterRead(@NonNull Book o) {
                        logger.info("Конец чтения");
                    }

                    public void onReadError(@NonNull Exception e) {
                        logger.info("Ошибка чтения");
                    }
                })
                .listener(new ItemWriteListener<MongoBook>() {
                    public void beforeWrite(@NonNull List<MongoBook> list) {
                        logger.info("Начало записи");
                    }

                    public void afterWrite(@NonNull List<MongoBook> list) {
                        logger.info("Конец записи");
                    }

                    public void onWriteError(@NonNull Exception e, @NonNull List<MongoBook> list) {
                        logger.info("Ошибка записи");
                    }
                })
                .listener(new ItemProcessListener<>() {
                    public void beforeProcess(@NonNull Book o) {
                        logger.info("Начало обработки");
                    }

                    public void afterProcess(@NonNull Book o, MongoBook o2) {
                        logger.info("Конец обработки");
                    }

                    public void onProcessError(@NonNull Book o, @NonNull Exception e) {
                        logger.info("Ошибка обработки");
                    }
                })
                .listener(new ChunkListener() {
                    public void beforeChunk(@NonNull ChunkContext chunkContext) {
                        logger.info("Начало пачки");
                    }

                    public void afterChunk(@NonNull ChunkContext chunkContext) {
                        logger.info("Конец пачки");
                    }

                    public void afterChunkError(@NonNull ChunkContext chunkContext) {
                        logger.info("Ошибка пачки");
                    }
                })
                .build();
    }

    @Bean
    public Job importBookJob(Step transformBookStep, Step cleanUpBookStep) {
        return new JobBuilder(IMPORT_BOOK_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(transformBookStep)
                .next(cleanUpBookStep)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(@NonNull JobExecution jobExecution) {
                        mongoAuthorService.fillCache();
                        mongoGenreService.fillCache();
                        logger.info("Начало job");
                    }

                    @Override
                    public void afterJob(@NonNull JobExecution jobExecution) {
                        logger.info("Конец job");
                    }
                })
                .build();
    }


    @Bean
    public MethodInvokingTaskletAdapter cleanUpBookTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();

        adapter.setTargetObject(cleanUpGenreService);
        adapter.setTargetMethod("cleanUp");

        return adapter;
    }

    @Bean
    public Step cleanUpBookStep() {
        return new StepBuilder("cleanUpBookStep", jobRepository)
                .tasklet(cleanUpBookTasklet(), platformTransactionManager)
                .build();
    }
}
