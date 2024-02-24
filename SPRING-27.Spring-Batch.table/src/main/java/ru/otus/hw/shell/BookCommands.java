package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.config.prop.AppProps;

import java.util.Properties;

import static ru.otus.hw.config.JobAuthorConfig.IMPORT_AUTHOR_JOB_NAME;
import static ru.otus.hw.config.JobBookConfig.IMPORT_BOOK_JOB_NAME;
import static ru.otus.hw.config.JobCommentConfig.IMPORT_COMMENT_JOB_NAME;
import static ru.otus.hw.config.JobGenreConfig.IMPORT_GENRE_JOB_NAME;


@SuppressWarnings("unused")
@RequiredArgsConstructor
@ShellComponent
public class BookCommands {

    private final AppProps appProps;

    private final Job importBookJob;

    private final JobLauncher jobLauncher;

    private final JobOperator jobOperator;

    private final JobExplorer jobExplorer;

    @SuppressWarnings("unused")
    @ShellMethod(value = "startBookMigrationJobWithJobLauncher", key = "book-sm-jl")
    public void startMigrationJobWithJobLauncher() throws Exception {
        // Book
        JobExecution execution = jobLauncher.run(importBookJob, new JobParametersBuilder()
                .addString("currentItemCount", appProps.getBook())
                .addString("jobNext", appProps.getJobNext())
                .toJobParameters());
        System.out.println(execution);
    }

    @SuppressWarnings("unused")
    @ShellMethod(value = "startBookMigrationJobWithJobOperator", key = "book-sm-jo")
    public void startMigrationJobWithJobOperator() throws Exception {
        // Book
        Properties properties = new Properties();
        properties.put("jobNext", appProps.getJobNext());
        properties.put("currentItemCount", appProps.getBook());
        long executionId = jobOperator.start(IMPORT_BOOK_JOB_NAME, properties);
        System.out.println(jobOperator.getSummary(executionId));
    }

    @SuppressWarnings("unused")
    @ShellMethod(value = "showBookInfo", key = "book-i")
    public void showInfo() {
        // Book
        System.out.println(jobExplorer.getJobNames());
        System.out.println(jobExplorer.getLastJobInstance(IMPORT_BOOK_JOB_NAME));
    }
}
