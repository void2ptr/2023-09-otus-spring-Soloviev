package ru.otus.hw;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.hw.data.InitTestData;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.hw.repositories", "ru.otus.hw.events"})
public abstract class AbstractInitTestData {
    protected final List<Author> dbAuthors = InitTestData.getDbAuthors();

    protected final List<Genre> dbGenres = InitTestData.getDbGenres();

    protected final List<Book> dbBooks = InitTestData.getDbBooks();
}
