package ru.otus.hw.events;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.BookRepository;


@Component
@RequiredArgsConstructor
public class MongoBookCascadeUpdateEventsListener extends AbstractMongoEventListener<Book>  {

    private final BookRepository bookRepository;

    @Override
    public void onAfterSave(@NonNull AfterSaveEvent<Book> event) {
        super.onAfterSave(event);
        Book book = event.getSource();
        bookRepository.updateCommentsByBook(book);
    }

    @Override
    public void onAfterDelete(@NonNull AfterDeleteEvent<Book> event) {
        super.onAfterDelete(event);
        val source = event.getSource();
        val id = source.get("_id").toString();
        bookRepository.removeCommentsByBookId(id);
    }
}
