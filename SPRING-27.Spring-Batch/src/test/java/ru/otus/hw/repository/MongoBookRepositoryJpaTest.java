package ru.otus.hw.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.hw.data.BooksArgumentsProvider;
import ru.otus.hw.data.InitTestData;
import ru.otus.hw.model.mongo.MongoAuthor;
import ru.otus.hw.model.mongo.MongoBook;
import ru.otus.hw.model.mongo.MongoGenre;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе MongoDB для работы с книгами")
@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.hw.repositories"})
class MongoBookRepositoryJpaTest {

    @Autowired
    private MongoBookRepository bookRepository;

    private List<MongoAuthor> dbAuthors;

    private List<MongoGenre> dbGenres;

    private List<MongoBook> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = InitTestData.getDbAuthors();
        dbGenres = InitTestData.getDbGenres();
        dbBooks = InitTestData.getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        // tested method
        List<MongoBook> actualBooks = bookRepository.findAll();
        List<MongoBook> expectedBooks = this.dbBooks;
        // sync List
        List<MongoBook> retrainBooks = new ArrayList<>(expectedBooks);
        retrainBooks.containsAll(actualBooks);

        assertThat(retrainBooks)
                .usingRecursiveComparison()
                .isEqualTo(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @ParameterizedTest
    @ArgumentsSource(BooksArgumentsProvider.class)
    void findByExternalLinkId(MongoBook book) {
        var expected = bookRepository.findByExternalLinkId(book.getExternalLink());
        assertThat(expected).isPresent();
        assertThat(expected.get().getTitle())
                .isEqualTo(book.getTitle());
    }
}
