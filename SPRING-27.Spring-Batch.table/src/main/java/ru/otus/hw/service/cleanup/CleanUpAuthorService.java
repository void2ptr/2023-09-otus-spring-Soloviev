package ru.otus.hw.service.cleanup;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.postgres.Author;
import ru.otus.hw.repository.postgres.AuthorRepository;
import ru.otus.hw.service.BatchLinkService;

import java.util.List;

import static ru.otus.hw.config.JobAuthorConfig.IMPORT_AUTHOR_JOB_NAME;

@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor
@Service
public class CleanUpAuthorService {

    protected final AuthorRepository authorRepository;

    protected final BatchLinkService batchLinkService;

    @SuppressWarnings("unused")
    public void cleanUp() {
        log.info("Выполняю завершающие мероприятия...");
        List<Long> exported = batchLinkService.saveAllImported(IMPORT_AUTHOR_JOB_NAME);
        List<Author> authors = authorRepository.findByIdIn(exported);
        authors.forEach(author -> author.setImported(true));
        authorRepository.saveAll(authors);

//        Thread.sleep(1000);
        log.info("Завершающие мероприятия закончены");
    }
}
