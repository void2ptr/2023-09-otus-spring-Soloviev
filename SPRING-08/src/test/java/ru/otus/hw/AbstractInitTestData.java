package ru.otus.hw;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import ru.otus.hw.dao.InitTestData;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.security.SecureRandom;
import java.util.List;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.hw.config", "ru.otus.hw.repositories", "ru.otus.hw.events"})
@Import(InitTestData.class)
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
        return new Comment(book,
                "Comment_1",
                "Comment_2",
                "Comment_3",
                "Comment_4",
                "Comment_5"
        );
    }
}
