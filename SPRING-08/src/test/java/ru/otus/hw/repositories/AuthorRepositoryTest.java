package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.hw.AbstractInitTestData;
import ru.otus.hw.dao.InitTestData;
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
        var expectedAuthor = InitTestData.getAuthors();

        assertThat(actualAuthor)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedAuthor);
        actualAuthor.forEach(System.out::println);
    }

    @DisplayName("должен искать авторов по списку имен")
    @ParameterizedTest
    @ValueSource(strings = {"Тит Ливий,Цезарь Гай Юлий", "Аппулей,Эзоп"})
    void findByFullNameIn(String fullNames) {
        String[] fullNamesSorted = fullNames.split(",");
        Arrays.sort(fullNamesSorted);
        List<String> expectedNameList = new ArrayList<>();
        Collections.addAll(expectedNameList, fullNamesSorted);

        // tested method
        List<Author> actualAuthor = authorRepository.findByFullNameIn(expectedNameList);
        Comparator<Author> compareByFullName = Comparator
                .comparing(Author::getFullName)
                .thenComparing(Author::getFullName);
        actualAuthor.sort(compareByFullName);

        assertThat(actualAuthor)
                .flatMap((Function<? super Author, ?>) Author::getFullName)
                .isEqualTo(expectedNameList);
    }
}
