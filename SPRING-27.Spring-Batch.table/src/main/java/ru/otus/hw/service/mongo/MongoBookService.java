package ru.otus.hw.service.mongo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.BatchLink;
import ru.otus.hw.model.mongo.MongoAuthor;
import ru.otus.hw.model.mongo.MongoBook;
import ru.otus.hw.model.mongo.MongoGenre;
import ru.otus.hw.model.postgres.Book;
import ru.otus.hw.repository.BatchLinkRepository;
import ru.otus.hw.repository.mongo.MongoBookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static ru.otus.hw.config.JobBookConfig.IMPORT_BOOK_JOB_NAME;

@SuppressWarnings("unused")
@Slf4j
@Service
@RequiredArgsConstructor
public class MongoBookService {


    private final MongoAuthorService mongoAuthorService;

    private final MongoGenreService mongoGenreService;

    private final MongoBookRepository mongoBookRepository;

    private final BatchLinkRepository batchLinkRepository;

    private ConcurrentHashMap<String, MongoBook> linksCache = new ConcurrentHashMap<>();

    public void fillCache() {
        Map<String, BatchLink> links = batchLinkRepository.findImportedByClassName(IMPORT_BOOK_JOB_NAME).stream()
                .collect(Collectors.toMap(BatchLink::getExportLink, batchLink -> batchLink, (a, b) -> b));
        mongoBookRepository.findAll().forEach(author -> {
            linksCache.put(links.get(author.getId()).getImportLink(), author);
        });
    }

    public MongoBook findById(String id) {
        return linksCache.get(id);
    }

    public MongoBook getMongoBook(Book book) {

        MongoAuthor mongoAuthor = mongoAuthorService.findById(book.getAuthor().getId().toString());

        List<MongoGenre> mongoGenres = new ArrayList<>();
        book.getGenres().forEach(genre -> {
            MongoGenre mongoGenre = mongoGenreService.findById(genre.getId().toString());
            mongoGenres.add(mongoGenre);
        });

        return new MongoBook(
                book.getTitle(),
                List.of(mongoAuthor),
                mongoGenres
        );
    }

}
