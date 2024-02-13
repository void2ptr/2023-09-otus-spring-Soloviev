package ru.otus.hw.service.processor;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import ru.otus.hw.model.mongo.MongoBook;
import ru.otus.hw.model.postgres.Book;
import ru.otus.hw.service.MongoBookService;

@RequiredArgsConstructor
public class BookProcessor implements ItemProcessor<Book, MongoBook> {

    private final MongoBookService bookService;

    @Override
    public MongoBook process(@NonNull Book book) throws Exception {
        return bookService.getMongoBook(book);
    }

}
