package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.mongo.MongoAuthor;
import ru.otus.hw.model.mongo.MongoBook;
import ru.otus.hw.model.mongo.MongoGenre;
import ru.otus.hw.model.postgres.Author;
import ru.otus.hw.model.postgres.Book;
import ru.otus.hw.model.postgres.Genre;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@Slf4j
@Service
@RequiredArgsConstructor
public class MongoBookService {

    private final BatchLinkService batchLinkService;

    public MongoBook getMongoBook(Book book) {

        Author author = book.getAuthor();
        MongoAuthor mongoAuthor = (MongoAuthor) batchLinkService.getExportLink(
                Author.class.getName(), author.getId().toString());

        List<MongoGenre> mongoGenres = new ArrayList<>();
        book.getGenres().forEach(genre -> {
            MongoGenre mongoGenre = (MongoGenre) batchLinkService.getExportLink(
                    Genre.class.getName(), genre.getId().toString());
            mongoGenres.add(mongoGenre);
        });

        return new MongoBook(
                book.getTitle(),
                List.of(mongoAuthor),
                mongoGenres
        );
    }

}
