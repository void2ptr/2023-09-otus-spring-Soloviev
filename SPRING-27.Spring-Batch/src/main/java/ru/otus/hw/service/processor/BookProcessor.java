package ru.otus.hw.service.processor;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import ru.otus.hw.model.mongo.MongoBook;
import ru.otus.hw.model.postgres.Book;
import ru.otus.hw.service.BatchLinkService;
import ru.otus.hw.service.MongoBookService;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public class BookProcessor implements ItemProcessor<Book, MongoBook> {

    private final MongoBookService bookService;

    private final BatchLinkService batchLinkService;

    @Override
    public MongoBook process(@NonNull Book book) {
        MongoBook mongoBook = bookService.getMongoBook(book);
        batchLinkService.setBatchLinkMap(Book.class.getName(), book.getId().toString(), mongoBook);
        return mongoBook;
    }

}
