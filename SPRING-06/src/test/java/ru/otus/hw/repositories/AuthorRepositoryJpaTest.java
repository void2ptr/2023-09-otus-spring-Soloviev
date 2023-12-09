package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import ru.otus.hw.data.AuthorsArgumentsProvider;
import ru.otus.hw.models.Author;
import ru.otus.hw.data.InitTestData;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с авторами книг")
@DataJpaTest
@Import({AuthorRepositoryJpa.class})
class AuthorRepositoryJpaTest {

    @Autowired
    private AuthorRepositoryJpa authorRepositoryJpa;

    @DisplayName("должен загружать список всех авторов")
    @Test
    void findAll() {
        // init
        var expectedAuthor = InitTestData.getDbAuthors();
        // method for test
        var actualAuthor = authorRepositoryJpa.findAllAuthors();
        // check
        assertThat(actualAuthor).containsExactlyElementsOf(expectedAuthor);
        actualAuthor.forEach(System.out::println);
    }

    @DisplayName("должен загружать авторов по id")
    @ParameterizedTest
    @ArgumentsSource(AuthorsArgumentsProvider.class)
    void findById(Author expectedAuthor) {
        // method for test
        var actualAuthor = authorRepositoryJpa.findAuthorById(expectedAuthor.getId());
        // check
        assertThat(actualAuthor).isPresent()
                .get()
                .isEqualTo(expectedAuthor);
    }

}