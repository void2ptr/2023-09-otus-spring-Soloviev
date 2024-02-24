package ru.otus.hw.service.cleanup;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.postgres.Book;
import ru.otus.hw.repository.postgres.BookRepository;
import ru.otus.hw.service.BatchLinkService;

import java.util.List;

import static ru.otus.hw.config.JobBookConfig.IMPORT_BOOK_JOB_NAME;


@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor
@Service
public class CleanUpBookService {

    protected final BookRepository bookRepository;

    protected final BatchLinkService batchLinkService;

    @SuppressWarnings("unused")
    public void cleanUp() {
        log.info("Выполняю завершающие мероприятия...");
        List<Long> exported = batchLinkService.saveAllImported(IMPORT_BOOK_JOB_NAME);
        List<Book> authors = bookRepository.findByIdIn(exported);
        authors.forEach(author -> author.setImported(true));
        bookRepository.saveAll(authors);

//        Thread.sleep(1000);
        log.info("Завершающие мероприятия закончены");
    }
}
