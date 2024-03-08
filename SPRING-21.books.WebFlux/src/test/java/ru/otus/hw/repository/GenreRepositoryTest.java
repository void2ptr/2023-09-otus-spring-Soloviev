package ru.otus.hw.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.otus.hw.data.GenresArgumentsProvider;
import ru.otus.hw.data.InitTestData;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.model.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе R2DBC для работы с жанрами книг")
@SpringBootTest
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    private List<GenreDto> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = InitTestData.getDbGenres();
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void findAll() {
        // init
        var expectedGenre = dbGenres.stream()
                .map(GenreMapper::toGenre)
                .toList();
        // method for test
        var actualAuthor = genreRepository.findAll()
                .collectList()
                .block();
        // check
        assertThat(actualAuthor).isNotNull()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedGenre);
        actualAuthor.forEach(System.out::println);
    }

    @DisplayName("должен загружать жанров по id")
    @ParameterizedTest
    @ArgumentsSource(GenresArgumentsProvider.class)
    void findGenresByIdIn(GenreDto expected) {
        // method for test
        var actual = genreRepository.findGenresByIdIn(List.of(expected.getId()))
                .map(GenreMapper::toDto)
                .blockFirst();
        // check
        assertThat(actual)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expected);
    }

    @DisplayName("должен загружать жанров по id (StepVerifier)")
    @ParameterizedTest
    @ArgumentsSource(GenresArgumentsProvider.class)
    void findGenresByIdInReact(GenreDto expected) {
        // method for test
        Flux<Genre> genreFlux = genreRepository.findGenresByIdIn(List.of(expected.getId()));
        genreFlux.log().subscribe(System.out::println);
        StepVerifier
                .create(genreFlux)
                .expectNextMatches(genre -> genre.getId().equals(expected.getId()))
                .expectComplete()
                .verify();
    }




}
