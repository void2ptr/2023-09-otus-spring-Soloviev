package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с авторами книг")
@JdbcTest
@Import({AuthorRepositoryJdbc.class})
class AuthorRepositoryJdbcTest {

    @Autowired
    private AuthorRepositoryJdbc authorRepositoryJdbc;

    private List<Author> dbAuthors;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
    }

    @DisplayName("должен загружать список всех авторов")
    @Test
    void findAll() {
        var actualAuthor = authorRepositoryJdbc.findAll();
        var expectedAuthor = dbAuthors;

        assertThat(actualAuthor).containsExactlyElementsOf(expectedAuthor);
        actualAuthor.forEach(System.out::println);
    }

    @DisplayName("должен загружать авторов по id")
    @ParameterizedTest
    @MethodSource("getDbAuthors")
    void findById(Author expectedAuthor) {
        var actualAuthor = authorRepositoryJdbc.findById(expectedAuthor.getId());
        assertThat(actualAuthor).isPresent()
                .get()
                .isEqualTo(expectedAuthor);
    }

    private static List<Author> getDbAuthors() {
        return InitTestData.getDbAuthors();
    }
}