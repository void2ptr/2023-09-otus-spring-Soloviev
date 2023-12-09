package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.hw.AbstractInitTestData;
import ru.otus.hw.data.AuthorsArgumentsProvider;
import ru.otus.hw.models.Author;

import java.util.*;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе MongoDB для работы с авторами книг")
class AuthorRepositoryTest extends AbstractInitTestData {

    @Autowired
    private AuthorRepository authorRepository;

    @DisplayName("должен загружать список всех авторов")
    @Test
    void findAll() {
        // tested method
        var actualAuthor = authorRepository.findAll();
        // check
        assertThat(actualAuthor)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(this.dbAuthors);
        actualAuthor.forEach(System.out::println);
    }

    @DisplayName("должен искать авторов по списку имен")
    @ParameterizedTest
    @ArgumentsSource(AuthorsArgumentsProvider.class)
    void findByFullNameIn(Author author) {
        // init
        List<String> expectedNameList = new ArrayList<>();
        Collections.addAll(expectedNameList, author.getFullName());
        // tested method
        List<Author> actualAuthor = authorRepository.findByFullNameIn(expectedNameList);
        // check
        Comparator<Author> compareByFullName = Comparator
                .comparing(Author::getFullName)
                .thenComparing(Author::getFullName);
        actualAuthor.sort(compareByFullName);

        assertThat(actualAuthor)
                .flatMap((Function<? super Author, ?>) Author::getFullName)
                .isEqualTo(expectedNameList);
    }
}
