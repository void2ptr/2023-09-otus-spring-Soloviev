package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с жанрами книг")
@DataJpaTest
@Import({GenreRepositoryJpa.class})
class GenreRepositoryJpaTest {

    @Autowired
    private GenreRepositoryJpa genreRepositoryJpa;

    private List<Genre> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = getDbGenres();
        List<Book> dbBooks = getDbBooks();
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void findAll() {
        // method for test
        var actualGenre = genreRepositoryJpa.findAll();
        var expectedGenre = dbGenres;

        assertThat(actualGenre).containsExactlyElementsOf(expectedGenre);
        actualGenre.forEach(System.out::println);
    }

    @DisplayName("должен загружать жанров по id")
    @ParameterizedTest
    @MethodSource("getDbGenres")
    void findAllByIds(Genre expectedGenre) {
        // method for test
        var actualGenres = genreRepositoryJpa.findAllByIds(List.of(expectedGenre.getId()));
        for (Genre actualGenre : actualGenres) {
            assertThat(actualGenre)
                    .isEqualTo(expectedGenre);
        }
    }

    private static List<Book> getDbBooks() {
        return InitTestData.getDbBooks();
    }

    private static List<Genre> getDbGenres() {
        return InitTestData.getDbGenres();
    }

}