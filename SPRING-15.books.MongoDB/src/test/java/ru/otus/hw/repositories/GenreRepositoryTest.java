package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.hw.AbstractInitTestData;
import ru.otus.hw.data.GenresArgumentsProvider;
import ru.otus.hw.data.InitTestData;
import ru.otus.hw.models.Genre;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе MongoDB для работы с жанрами книг")
class GenreRepositoryTest extends AbstractInitTestData {

    @Autowired
    private GenreRepository genreRepository;

    @DisplayName("должен загружать список всех жанров")
    @Test
    void findAll() {
        // tested method
        var actualGenre = genreRepository.findAll();
        var expectedGenre = InitTestData.getDbGenres();

        assertThat(actualGenre)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedGenre);
        actualGenre.forEach(System.out::println);
    }

    @DisplayName("должен искать жанры по списку названия")
    @ParameterizedTest
    @ArgumentsSource(GenresArgumentsProvider.class)
    void findByNameIn(Genre genre) {
        // init
        List<String> expectedGenres = new ArrayList<>();
        expectedGenres.add(genre.getName());
        assertThat(expectedGenres)
                .isNotEmpty();

        // tested method
        List<Genre> actualGenres = genreRepository.findByNameIn(expectedGenres);
        // check
        assertThat(actualGenres)
                .isNotEmpty()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(genre));
    }

}
