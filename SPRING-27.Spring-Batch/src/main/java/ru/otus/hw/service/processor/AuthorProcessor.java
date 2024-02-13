package ru.otus.hw.service.processor;


import org.springframework.batch.item.ItemProcessor;
import ru.otus.hw.model.mongo.MongoAuthor;
import ru.otus.hw.model.postgres.Author;

public class AuthorProcessor implements ItemProcessor<Author, MongoAuthor> {

    @Override
    public MongoAuthor process(Author author) throws Exception {
        return new MongoAuthor(author.getFullName(), author.getId().toString());
    }

}
