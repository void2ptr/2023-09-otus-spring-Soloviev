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
import ru.otus.hw.model.mongo.MongoAuthor;
import ru.otus.hw.model.mongo.MongoGenre;
import ru.otus.hw.model.postgres.Author;
import ru.otus.hw.model.postgres.Genre;
import ru.otus.hw.service.BatchLinkService;
import ru.otus.hw.service.cleanup.CleanUpGenreService;
import ru.otus.hw.service.processor.GenreProcessor;

import java.util.List;


@SuppressWarnings("unused")
@RequiredArgsConstructor
@Configuration
public class JobGenreConfig {

    public static final String IMPORT_GENRE_JOB_NAME = "importGenreJob";

    private static final int CHUNK_SIZE = 5;

    private final Logger logger = LoggerFactory.getLogger("Batch");

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final EntityManagerFactory postgresqlEntityManagerFactory;

    private final BatchLinkService batchLinkService;

    private final CleanUpGenreService cleanUpGenreService;

    @StepScope()
    @Bean
    public JpaPagingItemReader<Genre> genreItemReader(
            @Value("#{jobParameters['currentItemCount']}") String currentItemCount) {
        return new JpaPagingItemReaderBuilder<Genre>()
                .name("genreItemReader")
                .entityManagerFactory(postgresqlEntityManagerFactory)
                .queryString("SELECT g FROM Genre g WHERE g.imported = false")
                .currentItemCount(Integer.parseInt(currentItemCount))
                .pageSize(CHUNK_SIZE)
                .build();
    }

    @StepScope()
    @Bean
    public GenreProcessor genreProcessor() {
        return new GenreProcessor(batchLinkService);
    }

    @StepScope()
    @Bean
    public MongoItemWriter<MongoGenre> genreItemWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<MongoGenre>()
                .template(mongoTemplate)
                .collection("genres")
                .build();
    }

    @Bean
    public Step transformGenreStep(
            JpaPagingItemReader<Genre> reader,
            MongoItemWriter<MongoGenre> writer,
            ItemProcessor<Genre, MongoGenre> processor) {

        return new StepBuilder("transformGenreStep", jobRepository)
                .<Genre, MongoGenre>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(new ItemReadListener<>() {
                    public void beforeRead() {
                        logger.info("Начало чтения");
                    }

                    public void afterRead(@NonNull Genre o) {
                        logger.info("Конец чтения");
                    }

                    public void onReadError(@NonNull Exception e) {
                        logger.info("Ошибка чтения");
                    }
                })
                .listener(new ItemWriteListener<MongoGenre>() {
                    public void beforeWrite(@NonNull List<MongoGenre> list) {
                        logger.info("Начало записи");
                    }

                    public void afterWrite(@NonNull List<MongoGenre> list) {
                        logger.info("Конец записи");
                    }

                    public void onWriteError(@NonNull Exception e, @NonNull List<MongoGenre> list) {
                        logger.info("Ошибка записи");
                    }
                })
                .listener(new ItemProcessListener<>() {
                    public void beforeProcess(@NonNull Genre o) {
                        logger.info("Начало обработки");
                    }

                    public void afterProcess(@NonNull Genre o, MongoGenre o2) {
                        logger.info("Конец обработки");
                    }

                    public void onProcessError(@NonNull Genre o, @NonNull Exception e) {
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
    public Job importGenreJob(Step transformGenreStep, Step cleanUpGenreStep) {
        return new JobBuilder(IMPORT_GENRE_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(transformGenreStep)
                .next(cleanUpGenreStep)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(@NonNull JobExecution jobExecution) {
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
    public MethodInvokingTaskletAdapter cleanUpGenreTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();

        adapter.setTargetObject(cleanUpGenreService);
        adapter.setTargetMethod("cleanUp");

        return adapter;
    }

    @Bean
    public Step cleanUpGenreStep() {
        return new StepBuilder("cleanUpGenreStep", jobRepository)
                .tasklet(cleanUpGenreTasklet(), platformTransactionManager)
                .build();
    }

}
