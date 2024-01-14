//package ru.otus.hw.repository;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ArgumentsSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import ru.otus.hw.data.GenresArgumentsProvider;
//import ru.otus.hw.data.InitTestData;
//import ru.otus.hw.model.Genre;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DisplayName("Репозиторий на основе Jpa для работы с жанрами книг")
//@DataJpaTest
////@Import({GenreRepository.class})
//class GenreRepositoryJpaTest {
//
//    @Autowired
//    private GenreRepository genreRepository;
//
//    private List<Genre> dbGenres;
//
//    @BeforeEach
//    void setUp() {
//        dbGenres = InitTestData.getDbGenres();
//    }
//
//    @DisplayName("должен загружать список всех жанров")
//    @Test
//    void findAll() {
//        // init
//        var expectedGenre = dbGenres;
//        // method for test
//        var actualGenre = genreRepository.findAll();
//        // check
//        assertThat(actualGenre).containsExactlyElementsOf(expectedGenre);
//        actualGenre.forEach(System.out::println);
//    }
//
//    @DisplayName("должен загружать жанров по id")
//    @ParameterizedTest
//    @ArgumentsSource(GenresArgumentsProvider.class)
//    void findAllByIds(Genre expectedGenre) {
//        // method for test
//        var actualGenres = genreRepository.findAllByIdIn(List.of(expectedGenre.getId()));
//        // check
//        actualGenres.forEach(actualGenre -> assertThat(actualGenre)
//                .isEqualTo(expectedGenre));
//    }
//
//}
