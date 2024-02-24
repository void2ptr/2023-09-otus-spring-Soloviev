package ru.otus.hw.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.ListableJobLocator;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.model.mongo.MongoAuthor;
import ru.otus.hw.model.mongo.MongoBook;
import ru.otus.hw.model.mongo.MongoComment;
import ru.otus.hw.model.mongo.MongoGenre;
import ru.otus.hw.model.postgres.Author;
import ru.otus.hw.model.postgres.Book;
import ru.otus.hw.model.postgres.Comment;
import ru.otus.hw.model.postgres.Genre;
import ru.otus.hw.repository.MongoAuthorRepository;
import ru.otus.hw.repository.MongoBookRepository;
import ru.otus.hw.repository.MongoCommentRepository;
import ru.otus.hw.repository.MongoGenreRepository;
import ru.otus.hw.service.BatchLinkService;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.hw.config.JobAuthorConfig.IMPORT_AUTHOR_JOB_NAME;
import static ru.otus.hw.config.JobBookConfig.IMPORT_BOOK_JOB_NAME;
import static ru.otus.hw.config.JobCommentConfig.IMPORT_COMMENT_JOB_NAME;
import static ru.otus.hw.config.JobGenreConfig.IMPORT_GENRE_JOB_NAME;


@SuppressWarnings("unused")
@SpringBootTest
@SpringBatchTest
@EnableEmbeddedMongo
class JobConfigTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private ListableJobLocator jobLocator;

    @Autowired
    private MongoAuthorRepository mongoAuthorRepository;

    @Autowired
    private MongoGenreRepository mongoGenreRepository;

    @Autowired
    private MongoBookRepository mongoBookRepository;

    @Autowired
    private MongoCommentRepository mongoCommentRepository;

    @Autowired
    private BatchLinkService batchLinkService;

    @BeforeEach
    void clearMetaData() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void testAuthorJob() throws Exception {
        // test - is Job by name exist
        jobLauncherTestUtils.setJob(jobLocator.getJob(IMPORT_AUTHOR_JOB_NAME));
        Job job = jobLauncherTestUtils.getJob();
        assertThat(job).isNotNull()
                .extracting(Job::getName)
                .isEqualTo(IMPORT_AUTHOR_JOB_NAME);

        // test - JobExecution
        JobParameters parameters = new JobParametersBuilder().toJobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(parameters);

        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
        // test - result
        List<MongoAuthor> expected = batchLinkService.<MongoAuthor>getObjects(Author.class.getName())
                .stream().sorted(Comparator.comparing(MongoAuthor::getId)).toList();
        List<MongoAuthor> actual = mongoAuthorRepository.findAll()
                .stream().sorted(Comparator.comparing(MongoAuthor::getId)).toList();
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expected);
        actual.forEach(System.out::println);
    }


    @Test
    void testGenreJob() throws Exception {
        // test - is Job by name exist
        jobLauncherTestUtils.setJob(jobLocator.getJob(IMPORT_GENRE_JOB_NAME));
        Job job = jobLauncherTestUtils.getJob();
        assertThat(job).isNotNull()
                .extracting(Job::getName)
                .isEqualTo(IMPORT_GENRE_JOB_NAME);

        // test - JobExecution
        JobParameters parameters = new JobParametersBuilder().toJobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(parameters);

        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
        // test - result
        List<MongoGenre> expected = batchLinkService.<MongoGenre>getObjects(Genre.class.getName())
                .stream().sorted(Comparator.comparing(MongoGenre::getId)).toList();
        List<MongoGenre> actual = mongoGenreRepository.findAll()
                .stream().sorted(Comparator.comparing(MongoGenre::getId)).toList();
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expected);
        actual.forEach(System.out::println);
    }

    @Nested
    class FirstNestedClass {
        @Test
        void testBookJob() throws Exception {
            // test - is Job by name exist
            jobLauncherTestUtils.setJob(jobLocator.getJob(IMPORT_BOOK_JOB_NAME));
            Job job = jobLauncherTestUtils.getJob();
            assertThat(job).isNotNull()
                    .extracting(Job::getName)
                    .isEqualTo(IMPORT_BOOK_JOB_NAME);

            // test - JobExecution
            JobParameters parameters = new JobParametersBuilder().toJobParameters();
            JobExecution jobExecution = jobLauncherTestUtils.launchJob(parameters);

            assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
            // test - result
            List<MongoBook> expected = batchLinkService.<MongoBook>getObjects(Book.class.getName())
                    .stream().sorted(Comparator.comparing(MongoBook::getId)).toList();
            List<MongoBook> actual = mongoBookRepository.findAll()
                    .stream().sorted(Comparator.comparing(MongoBook::getId)).toList();
            assertThat(actual)
                    .usingRecursiveComparison()
                    .ignoringExpectedNullFields()
                    .isEqualTo(expected);
            actual.forEach(System.out::println);
        }

        @Nested
        class SecondNestedClass{
            @Test
            void testCommentJob() throws Exception {
                // test - is Job by name exist
                jobLauncherTestUtils.setJob(jobLocator.getJob(IMPORT_COMMENT_JOB_NAME));
                Job job = jobLauncherTestUtils.getJob();
                assertThat(job).isNotNull()
                        .extracting(Job::getName)
                        .isEqualTo(IMPORT_COMMENT_JOB_NAME);

                // test - JobExecution
                JobParameters parameters = new JobParametersBuilder().toJobParameters();
                JobExecution jobExecution = jobLauncherTestUtils.launchJob(parameters);

                assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
                // test - result
                List<MongoComment> expected = batchLinkService.<MongoComment>getObjects(Comment.class.getName())
                        .stream().sorted(Comparator.comparing(MongoComment::getId)).toList();
                List<MongoComment> actual = mongoCommentRepository.findAll()
                        .stream().sorted(Comparator.comparing(MongoComment::getId)).toList();
                assertThat(actual)
                        .usingRecursiveComparison()
                        .ignoringExpectedNullFields()
                        .isEqualTo(expected);
                actual.forEach(System.out::println);
            }
        }
    }
}