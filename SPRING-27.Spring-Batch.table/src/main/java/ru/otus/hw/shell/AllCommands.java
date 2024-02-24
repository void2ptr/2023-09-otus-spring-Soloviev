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
public class AllCommands {

    private final AppProps appProps;

    private final Job importAuthorJob;

    private final Job importGenreJob;

    private final Job importBookJob;

    private final Job importCommentJob;

    private final JobLauncher jobLauncher;

    private final JobOperator jobOperator;

    private final JobExplorer jobExplorer;

    @SuppressWarnings("unused")
    @ShellMethod(value = "startMigrationJobWithJobLauncher", key = "sm-jl")
    public void startMigrationJobWithJobLauncher() throws Exception {
        // Author
        JobExecution execution = jobLauncher.run(importAuthorJob, new JobParametersBuilder()
                .addString("currentItemCount", appProps.getAuthor())
                .addString("jobNext", appProps.getJobNext())
                .toJobParameters());
        System.out.println(execution);
        // Genre
        execution = jobLauncher.run(importGenreJob, new JobParametersBuilder()
                .addString("currentItemCount", appProps.getGenre())
                .addString("jobNext", appProps.getJobNext())
                .toJobParameters());
        System.out.println(execution);
        // Book
        execution = jobLauncher.run(importBookJob, new JobParametersBuilder()
                .addString("currentItemCount", appProps.getBook())
                .addString("jobNext", appProps.getJobNext())
                .toJobParameters());
        System.out.println(execution);
        // Comment
        execution = jobLauncher.run(importCommentJob, new JobParametersBuilder()
                .addString("currentItemCount", appProps.getComment())
                .addString("jobNext", appProps.getJobNext())
                .toJobParameters());
        System.out.println(execution);
    }

    @SuppressWarnings("unused")
    @ShellMethod(value = "startMigrationJobWithJobOperator", key = "sm-jo")
    public void startMigrationJobWithJobOperator() throws Exception {
        // Author
        Properties properties = new Properties();
        properties.put("jobNext", appProps.getJobNext());
        properties.put("currentItemCount", appProps.getAuthor());
        long executionId = jobOperator.start(IMPORT_AUTHOR_JOB_NAME, properties);
        System.out.println(jobOperator.getSummary(executionId));
        // Genre
//        properties = new Properties();
        properties.put("currentItemCount", appProps.getGenre());
        executionId = jobOperator.start(IMPORT_GENRE_JOB_NAME, properties);
        System.out.println(jobOperator.getSummary(executionId));
        // Book
//        properties = new Properties();
        properties.put("currentItemCount", appProps.getBook());
        executionId = jobOperator.start(IMPORT_BOOK_JOB_NAME, properties);
        System.out.println(jobOperator.getSummary(executionId));
        // Comment
//        properties = new Properties();
        properties.put("currentItemCount", appProps.getComment());
        executionId = jobOperator.start(IMPORT_COMMENT_JOB_NAME, properties);
        System.out.println(jobOperator.getSummary(executionId));

    }

    @SuppressWarnings("unused")
    @ShellMethod(value = "showInfo", key = "i")
    public void showInfo() {
        // Author
        System.out.println(jobExplorer.getJobNames());
        System.out.println(jobExplorer.getLastJobInstance(IMPORT_AUTHOR_JOB_NAME));
        // Genre
        System.out.println(jobExplorer.getJobNames());
        System.out.println(jobExplorer.getLastJobInstance(IMPORT_GENRE_JOB_NAME));
        // Book
        System.out.println(jobExplorer.getJobNames());
        System.out.println(jobExplorer.getLastJobInstance(IMPORT_BOOK_JOB_NAME));
        // Comment
        System.out.println(jobExplorer.getJobNames());
        System.out.println(jobExplorer.getLastJobInstance(IMPORT_COMMENT_JOB_NAME));
    }
}
