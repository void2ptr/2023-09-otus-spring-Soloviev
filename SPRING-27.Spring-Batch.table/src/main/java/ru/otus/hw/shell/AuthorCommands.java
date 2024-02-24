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


@SuppressWarnings("unused")
@RequiredArgsConstructor
@ShellComponent
public class AuthorCommands {

    private final AppProps appProps;

    private final Job importAuthorJob;

    private final JobLauncher jobLauncher;

    private final JobOperator jobOperator;

    private final JobExplorer jobExplorer;

    @SuppressWarnings("unused")
    @ShellMethod(value = "startAuthorMigrationJobWithJobLauncher", key = "author-sm-jl")
    public void startMigrationJobWithJobLauncher() throws Exception {
        // Author
        JobExecution execution = jobLauncher.run(importAuthorJob, new JobParametersBuilder()
                .addString("currentItemCount", appProps.getAuthor())
                .addString("jobNext", appProps.getJobNext())
                .toJobParameters());
        System.out.println(execution);
    }

    @SuppressWarnings("unused")
    @ShellMethod(value = "startAuthorMigrationJobWithJobOperator", key = "author-sm-jo")
    public void startMigrationJobWithJobOperator() throws Exception {
        // Author
        Properties properties = new Properties();
        properties.put("jobNext", appProps.getJobNext());
        properties.put("currentItemCount", appProps.getAuthor());
        long executionId = jobOperator.start(IMPORT_AUTHOR_JOB_NAME, properties);
        System.out.println(jobOperator.getSummary(executionId));
    }

    @SuppressWarnings("unused")
    @ShellMethod(value = "showAuthorInfo", key = "author-i")
    public void showInfo() {
        // Author
        System.out.println(jobExplorer.getJobNames());
        System.out.println(jobExplorer.getLastJobInstance(IMPORT_AUTHOR_JOB_NAME));
    }
}
