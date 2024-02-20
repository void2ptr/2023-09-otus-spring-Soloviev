package ru.otus.hw.service.processor;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import ru.otus.hw.model.mongo.MongoGenre;
import ru.otus.hw.model.postgres.Genre;
import ru.otus.hw.service.BatchLinkService;


@RequiredArgsConstructor
public class GenreProcessor implements ItemProcessor<Genre, MongoGenre> {

    private final BatchLinkService batchLinkService;

    @Override
    public MongoGenre process(Genre genre) {
        MongoGenre mongoGenre = new MongoGenre(genre.getName());
        batchLinkService.setBatchLinkMap(Genre.class.getName(), genre.getId().toString(), mongoGenre);
        return mongoGenre;
    }

}
