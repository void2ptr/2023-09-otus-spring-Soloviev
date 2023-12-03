package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.hw.AbstractInitTestData;
import ru.otus.hw.dao.InitTestData;
import ru.otus.hw.models.Genre;

import java.util.*;
import java.util.function.Function;

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
    @ValueSource(strings = {"опус,эпиграммы", "поэма,ода,новелла"})
    void findByNameIn(String genres) {
        String[] genresSorted = genres.split(",");
        Arrays.sort(genresSorted);
        List<String> expectedGenres = new ArrayList<>();
        Collections.addAll(expectedGenres, genresSorted);

        // tested method
        List<Genre> actualGenres = genreRepository.findByNameIn(expectedGenres);
        Comparator<Genre> compareByFullName = Comparator
                .comparing(Genre::getName)
                .thenComparing(Genre::getName);
        actualGenres.sort(compareByFullName);

        assertThat(actualGenres)
                .flatMap((Function<? super Genre, ?>) Genre::getName)
                .isEqualTo(expectedGenres);
    }

}
