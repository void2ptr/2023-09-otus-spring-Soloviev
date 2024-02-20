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
import ru.otus.hw.model.mongo.MongoComment;
import ru.otus.hw.model.postgres.Comment;
import ru.otus.hw.service.BatchLinkService;
import ru.otus.hw.service.CleanUpService;
import ru.otus.hw.service.processor.CommentProcessor;


@SuppressWarnings("unused")
@RequiredArgsConstructor
@Configuration
public class JobCommentConfig {

    public static final String IMPORT_COMMENT_JOB_NAME = "importCommentJob";

    private static final int  CHUNK_SIZE = 5;

    private final Logger logger = LoggerFactory.getLogger("Batch");

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final EntityManagerFactory postgresqlEntityManagerFactory;

    private final BatchLinkService batchLinkService;

    private final CleanUpService cleanUpService;

    @Bean
    public JpaPagingItemReader<Comment> commentItemReader() {
        return new JpaPagingItemReaderBuilder<Comment>()
                .name("commentItemReader")
                .entityManagerFactory(postgresqlEntityManagerFactory)
                .queryString("SELECT a FROM Comment a")
                .pageSize(CHUNK_SIZE)
                .build();
    }

    @Bean
    public CommentProcessor commentItemProcessor() {
        return new CommentProcessor(batchLinkService);
    }

    @Bean
    public MongoItemWriter<MongoComment> commentItemWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<MongoComment>()
                .template(mongoTemplate)
                .collection("comments")
                .build();
    }

    @Bean
    public Step transformCommentStep(
            JpaPagingItemReader<Comment> reader,
            MongoItemWriter<MongoComment> writer,
            ItemProcessor<Comment, MongoComment> processor) {
        return new StepBuilder("transformCommentStep", jobRepository)
                .<Comment, MongoComment>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job importCommentJob(Step transformCommentStep, Step cleanUpCommentStep) {
        return new JobBuilder(IMPORT_COMMENT_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(transformCommentStep)
                .next(cleanUpCommentStep)
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
    public MethodInvokingTaskletAdapter cleanUpCommentTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();

        adapter.setTargetObject(cleanUpService);
        adapter.setTargetMethod("cleanUp");

        return adapter;
    }

    @Bean
    public Step cleanUpCommentStep() {
        return new StepBuilder("cleanUpCommentStep", jobRepository)
                .tasklet(cleanUpCommentTasklet(), platformTransactionManager)
                .build();
    }

}
