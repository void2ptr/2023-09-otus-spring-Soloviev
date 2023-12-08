package ru.otus.hw;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.hw.config", "ru.otus.hw.repositories", "ru.otus.hw.events"})
public abstract class AbstractInitTestData {
    protected List<Author> dbAuthors;

    protected List<Genre> dbGenres;

    protected List<Book> dbBooks;

    protected Book bookRandomGenerator() {
        SecureRandom random = new SecureRandom();
        return new Book(
                "BookTitle_" + random.nextInt(dbBooks.size()),
                List.of(dbAuthors.get(random.nextInt(dbAuthors.size())),
                        dbAuthors.get(random.nextInt(dbAuthors.size()))),
                List.of(dbGenres.get(random.nextInt(dbGenres.size())),
                        dbGenres.get(random.nextInt(dbGenres.size()))));
    }

    protected Comment commentRandomGenerator(Book book) {
        SecureRandom random = new SecureRandom();
        return new Comment(book,
                IntStream.range(1, random.nextInt(100)).boxed()
                        .map(id -> "Comment_" + random.nextInt(1000))
                        .collect(Collectors.toList())
        );
    }
}
