package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.mongo.MongoAuthor;
import ru.otus.hw.model.mongo.MongoBook;
import ru.otus.hw.model.mongo.MongoGenre;
import ru.otus.hw.model.postgres.Book;
import ru.otus.hw.repository.MongoAuthorRepository;
import ru.otus.hw.repository.MongoBookRepository;
import ru.otus.hw.repository.MongoGenreRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MongoBookService {

    private final MongoBookRepository mongoBookRepository;

    private final MongoAuthorRepository mongoAuthorRepository;

    private final MongoGenreRepository mongoGenreRepository;

    public MongoBook getMongoBook(Book book) {
        MongoAuthor mongoAuthor = mongoAuthorRepository.findByExternalLinkId(book.getAuthor().getId().toString())
                .orElseThrow(() ->
                        new RuntimeException("Author not found %d".formatted(book.getAuthor().getId())));
        List<MongoGenre> mongoGenres = new ArrayList<>();
        book.getGenres().forEach(genre -> {
            MongoGenre mongoGenre = mongoGenreRepository.findByExternalLinkId(genre.getId().toString())
                    .orElseThrow(() ->
                            new RuntimeException("Genre not found %d" .formatted(genre.getId())));
            mongoGenres.add(mongoGenre);
        });

        return new MongoBook(
                book.getTitle(),
                List.of(mongoAuthor),
                mongoGenres,
                book.getId().toString()
        );
    }

    public MongoBook getMongoBook(String externalLink) {
        return mongoBookRepository.findByExternalLinkId(externalLink)
                .orElseThrow(() ->
                        new RuntimeException("Book not found %s" .formatted(externalLink)));
    }
}
