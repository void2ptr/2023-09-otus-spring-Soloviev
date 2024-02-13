package ru.otus.hw.service.processor;


import org.springframework.batch.item.ItemProcessor;
import ru.otus.hw.model.mongo.MongoGenre;
import ru.otus.hw.model.postgres.Genre;

public class GenreProcessor implements ItemProcessor<Genre, MongoGenre> {

    @Override
    public MongoGenre process(Genre genre) throws Exception {
        return new MongoGenre(genre.getName(), genre.getId().toString());
    }

}
