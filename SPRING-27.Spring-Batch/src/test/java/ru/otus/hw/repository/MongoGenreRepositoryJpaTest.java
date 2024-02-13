package ru.otus.hw.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.hw.data.BooksArgumentsProvider;
import ru.otus.hw.data.GenresArgumentsProvider;
import ru.otus.hw.data.InitTestData;
import ru.otus.hw.model.mongo.MongoBook;
import ru.otus.hw.model.mongo.MongoGenre;
import ru.otus.hw.model.postgres.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе MongoDB для работы с жанрами книг")
@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.hw.repositories"})
class MongoGenreRepositoryJpaTest {

    @Autowired
    private MongoGenreRepository genreRepository;

    private List<MongoGenre> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = InitTestData.getDbGenres();
    }

    @DisplayName("должен загружать жанр по link")
    @ParameterizedTest
    @ArgumentsSource(GenresArgumentsProvider.class)
    void findByExternalLinkId(MongoGenre genre) {
        var expected = genreRepository.findByExternalLinkId(genre.getExternalLink());
        assertThat(expected).isPresent();
        assertThat(expected.get().getName())
                .isEqualTo(genre.getName());
    }
}
