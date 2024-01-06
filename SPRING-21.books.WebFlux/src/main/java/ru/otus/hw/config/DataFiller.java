//package ru.otus.hw.config;
//
//import java.util.Arrays;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//import reactor.core.scheduler.Scheduler;
//import ru.otus.hw.model.Author;
//import ru.otus.hw.repository.AuthorRepository;
//
//@Component
//public class DataFiller implements ApplicationRunner {
//    private static final Logger logger = LoggerFactory.getLogger(DataFiller.class);
//
//    private final AuthorRepository authorRepository;
//    private final Scheduler workerPool;
//
//    public DataFiller(AuthorRepository authorRepository, Scheduler workerPool) {
//        this.authorRepository = authorRepository;
//        this.workerPool = workerPool;
//    }
//
//    @Override
//    public void run(ApplicationArguments args) {
//        authorRepository.saveAll(Arrays.asList(
//                        new Author(22,"Pushkin"),
//                        new Author(23,"Lermontov"),
//                        new Author(60,"Tolstoy")
//                )).publishOn(workerPool)
//                .subscribe(savedPerson -> logger.info("saved person:{}", savedPerson));
//    }
//}
