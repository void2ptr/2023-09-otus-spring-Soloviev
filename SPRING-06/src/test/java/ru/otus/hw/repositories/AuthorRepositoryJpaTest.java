package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import ru.otus.hw.models.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с авторами книг")
@DataJpaTest
@Import({AuthorRepositoryJpa.class})
//@Transactional(propagation = Propagation.NEVER)
class AuthorRepositoryJpaTest {

    @Autowired
    private AuthorRepositoryJpa authorRepositoryJpa;

    private List<Author> dbAuthors;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
    }

    @DisplayName("должен загружать список всех авторов")
    @Test
    void findAll() {
        var actualAuthor = authorRepositoryJpa.findAll();
        var expectedAuthor = dbAuthors;

        assertThat(actualAuthor).containsExactlyElementsOf(expectedAuthor);
        actualAuthor.forEach(System.out::println);
    }

    @DisplayName("должен загружать авторов по id")
    @ParameterizedTest
    @MethodSource("getDbAuthors")
    void findById(Author expectedAuthor) {
        var actualAuthor = authorRepositoryJpa.findById(expectedAuthor.getId());
        assertThat(actualAuthor).isPresent()
                .get()
                .isEqualTo(expectedAuthor);
    }

    private static List<Author> getDbAuthors() {
        return InitTestData.getDbAuthors();
    }
}