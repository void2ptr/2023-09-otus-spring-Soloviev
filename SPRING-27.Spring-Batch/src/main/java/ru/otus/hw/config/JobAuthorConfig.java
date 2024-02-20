package ru.otus.hw.config;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.model.mongo.MongoAuthor;
import ru.otus.hw.model.postgres.Author;
import ru.otus.hw.service.BatchLinkService;
import ru.otus.hw.service.CleanUpService;
import ru.otus.hw.service.processor.AuthorProcessor;

@SuppressWarnings("unused")
@RequiredArgsConstructor
@Configuration
public class JobAuthorConfig {

    public static final String IMPORT_AUTHOR_JOB_NAME = "importAuthorJob";

    private static final int CHUNK_SIZE = 5;

    private final Logger logger = LoggerFactory.getLogger("Batch");

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final EntityManagerFactory postgresqlEntityManagerFactory;

    private final BatchLinkService batchLinkService;

    private final CleanUpService cleanUpService;

    @Bean
    public JpaPagingItemReader<Author> authorItemReader() {
        return new JpaPagingItemReaderBuilder<Author>()
                .name("authorItemReader")
                .entityManagerFactory(postgresqlEntityManagerFactory)
                .queryString("SELECT a FROM Author a")
                .pageSize(CHUNK_SIZE)
                .build();
    }

    @Bean
    public AuthorProcessor authorItemProcessor() {
        return new AuthorProcessor(batchLinkService);
    }

    @Bean
    public MongoItemWriter<MongoAuthor> authorItemWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<MongoAuthor>()
                .template(mongoTemplate)
                .collection("authors")
                .build();
    }

    @Bean
    public Step transformAuthorStep(
            JpaPagingItemReader<Author> reader,
            MongoItemWriter<MongoAuthor> writer,
            ItemProcessor<Author, MongoAuthor> processor) {

        return new StepBuilder("transformAuthorStep", jobRepository)
                .<Author, MongoAuthor>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job importAuthorJob(Step transformAuthorStep, Step cleanUpAuthorStep) {
        return new JobBuilder(IMPORT_AUTHOR_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(transformAuthorStep)
                .next(cleanUpAuthorStep)
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
    public MethodInvokingTaskletAdapter cleanUpAuthorTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();

        adapter.setTargetObject(cleanUpService);
        adapter.setTargetMethod("cleanUp");

        return adapter;
    }

    @Bean
    public Step cleanUpAuthorStep() {
        return new StepBuilder("cleanUpAuthorStep", jobRepository)
                .tasklet(cleanUpAuthorTasklet(), platformTransactionManager)
                .build();
    }
}
