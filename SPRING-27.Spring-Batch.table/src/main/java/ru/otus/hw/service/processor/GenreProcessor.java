package ru.otus.hw.service.processor;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import ru.otus.hw.model.mongo.MongoGenre;
import ru.otus.hw.model.postgres.Genre;
import ru.otus.hw.service.BatchLinkService;

import static ru.otus.hw.config.JobGenreConfig.IMPORT_GENRE_JOB_NAME;


@RequiredArgsConstructor
public class GenreProcessor implements ItemProcessor<Genre, MongoGenre> {

    private final BatchLinkService batchLinkService;

    @Override
    public MongoGenre process(Genre genre) {
        MongoGenre mongoGenre = new MongoGenre(genre.getName());
        batchLinkService.insert(IMPORT_GENRE_JOB_NAME, genre.getId().toString(), mongoGenre.getId());
        return mongoGenre;
    }

}
