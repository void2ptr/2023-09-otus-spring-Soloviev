package ru.otus.hw.service.processor;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import ru.otus.hw.model.mongo.MongoBook;
import ru.otus.hw.model.postgres.Book;
import ru.otus.hw.service.BatchLinkService;
import ru.otus.hw.service.mongo.MongoBookService;

import static ru.otus.hw.config.JobBookConfig.IMPORT_BOOK_JOB_NAME;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public class BookProcessor implements ItemProcessor<Book, MongoBook> {

    private final BatchLinkService batchLinkService;

    private final MongoBookService mongoBookService;


    @Override
    public MongoBook process(@NonNull Book book) {
        MongoBook mongoBook = mongoBookService.getMongoBook(book);
        batchLinkService.insert(IMPORT_BOOK_JOB_NAME, book.getId().toString(), mongoBook.getId());
        return mongoBook;
    }

}
