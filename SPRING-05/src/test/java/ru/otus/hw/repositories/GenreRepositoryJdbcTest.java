package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с жанрами книг")
@JdbcTest
@Import({GenreRepositoryJdbc.class})
class GenreRepositoryJdbcTest {

    @Autowired
    private GenreRepositoryJdbc genreRepositoryJdbc;

    private List<Genre> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = getDbGenres();
        List<Book> dbBooks = getDbBooks();
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void findAll() {
        var actualGenre = genreRepositoryJdbc.findAll();
        var expectedGenre = dbGenres;

        assertThat(actualGenre).containsExactlyElementsOf(expectedGenre);
        actualGenre.forEach(System.out::println);
    }

    @DisplayName("должен загружать жанров по id")
    @ParameterizedTest
    @MethodSource("getDbGenres")
    void findAllByIds(Genre expectedGenre) {
        var actualGenres = genreRepositoryJdbc.findAllByIds(List.of(expectedGenre.getId()));
        for (Genre actualGenre : actualGenres) {
            assertThat(actualGenre)
                    .isEqualTo(expectedGenre);
        }
    }

    @DisplayName("должен загружать жанров из книг")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void findBookGenres(Book book) {
        var expectedGenres = book.getGenres();
        var actualGenres = genreRepositoryJdbc.findBookGenres(book.getId());
        assertThat(actualGenres).isEqualTo(expectedGenres);
    }

    private static List<Book> getDbBooks() {
        return InitDB.getDbBooks();
    }

    private static List<Genre> getDbGenres() {
        return InitDB.getDbGenres();
    }

}