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
public class CommandCommands {

    private final AppProps appProps;

    private final Job importCommentJob;

    private final JobLauncher jobLauncher;

    private final JobOperator jobOperator;

    private final JobExplorer jobExplorer;

    @SuppressWarnings("unused")
    @ShellMethod(value = "startCommentMigrationJobWithJobLauncher", key = "comment-sm-jl")
    public void startMigrationJobWithJobLauncher() throws Exception {
        // Comment
        JobExecution execution = jobLauncher.run(importCommentJob, new JobParametersBuilder()
                .addString("currentItemCount", appProps.getComment())
                .addString("jobNext", appProps.getJobNext())
                .toJobParameters());
        System.out.println(execution);
    }

    @SuppressWarnings("unused")
    @ShellMethod(value = "startCommentMigrationJobWithJobOperator", key = "comment-sm-jo")
    public void startMigrationJobWithJobOperator() throws Exception {
        // Comment
        Properties properties = new Properties();
        properties.put("jobNext", appProps.getJobNext());
        properties.put("currentItemCount", appProps.getComment());
        long executionId = jobOperator.start(IMPORT_COMMENT_JOB_NAME, properties);
        System.out.println(jobOperator.getSummary(executionId));

    }

    @SuppressWarnings("unused")
    @ShellMethod(value = "showCommentInfo", key = "comment-i")
    public void showInfo() {
        // Comment
        System.out.println(jobExplorer.getJobNames());
        System.out.println(jobExplorer.getLastJobInstance(IMPORT_COMMENT_JOB_NAME));
    }
}
