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
import ru.otus.hw.model.mongo.MongoGenre;
import ru.otus.hw.model.postgres.Genre;
import ru.otus.hw.service.processor.GenreProcessor;

@RequiredArgsConstructor
@Configuration
public class JobGenreConfig {

    public static final String IMPORT_GENRE_JOB_NAME = "importGenreJob";

    private static final int CHUNK_SIZE = 5;

    private final Logger logger = LoggerFactory.getLogger("Batch");

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final EntityManagerFactory postgresqlEntityManagerFactory;

    @Bean
    public JpaPagingItemReader<Genre> genreItemReader() {
        return new JpaPagingItemReaderBuilder<Genre>()
                .name("genreItemReader")
                .entityManagerFactory(postgresqlEntityManagerFactory)
                .queryString("SELECT a FROM Genre a")
                .pageSize(CHUNK_SIZE)
                .build();
    }

    @Bean
    public GenreProcessor genreProcessor() {
        return new GenreProcessor();
    }

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
            ItemProcessor<Genre, MongoGenre> processor) throws Exception {

        return new StepBuilder("transformGenreStep", jobRepository)
                .<Genre, MongoGenre>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job importGenreJob(Step transformGenreStep, Step cleanUpStep) {
        return new JobBuilder(IMPORT_GENRE_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(transformGenreStep)
                .next(cleanUpStep)
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
}
