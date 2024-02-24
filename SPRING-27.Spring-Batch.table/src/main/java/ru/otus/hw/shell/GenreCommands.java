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

import static ru.otus.hw.config.JobGenreConfig.IMPORT_GENRE_JOB_NAME;


@SuppressWarnings("unused")
@RequiredArgsConstructor
@ShellComponent
public class GenreCommands {

    private final AppProps appProps;

    private final Job importGenreJob;

    private final JobLauncher jobLauncher;

    private final JobOperator jobOperator;

    private final JobExplorer jobExplorer;

    @SuppressWarnings("unused")
    @ShellMethod(value = "startGenreMigrationJobWithJobLauncher", key = "genre-sm-jl")
    public void startMigrationJobWithJobLauncher() throws Exception {
        // Genre
        JobExecution execution = jobLauncher.run(importGenreJob, new JobParametersBuilder()
                .addString("currentItemCount", appProps.getGenre())
                .addString("jobNext", appProps.getJobNext())
                .toJobParameters());
        System.out.println(execution);
    }

    @SuppressWarnings("unused")
    @ShellMethod(value = "startGenreMigrationJobWithJobOperator", key = "genre-sm-jo")
    public void startMigrationJobWithJobOperator() throws Exception {
        // Genre
        Properties properties = new Properties();
        properties.put("jobNext", appProps.getJobNext());
        properties.put("currentItemCount", appProps.getGenre());
        long executionId = jobOperator.start(IMPORT_GENRE_JOB_NAME, properties);
        System.out.println(jobOperator.getSummary(executionId));
    }

    @SuppressWarnings("unused")
    @ShellMethod(value = "showGenreInfo", key = "genre-i")
    public void showInfo() {
        // Genre
        System.out.println(jobExplorer.getJobNames());
        System.out.println(jobExplorer.getLastJobInstance(IMPORT_GENRE_JOB_NAME));
    }
}
