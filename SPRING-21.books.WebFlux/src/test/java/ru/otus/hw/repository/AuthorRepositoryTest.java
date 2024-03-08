package ru.otus.hw.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.data.AuthorsArgumentsProvider;
import ru.otus.hw.data.InitTestData;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.mapper.AuthorMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе R2DBC для работы с авторами книг")
@SpringBootTest
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    private List<AuthorDto> dbAuthors;

    @BeforeEach
    void setUp() {
        dbAuthors = InitTestData.getDbAuthors();
    }

    @DisplayName("должен загружать список всех авторов")
    @Test
    void findAll() {
        // init
        var expectedAuthor = dbAuthors.stream()
                .map(AuthorMapper::toAuthor)
                .toList();
        // method for test
        var actualAuthor = authorRepository.findAll()
                .collectList()
                .block();
        // check
        assertThat(actualAuthor).isNotNull()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedAuthor);
        actualAuthor.forEach(System.out::println);
    }

    @DisplayName("должен загружать авторов по id")
    @ParameterizedTest
    @ArgumentsSource(AuthorsArgumentsProvider.class)
    void findById(AuthorDto authorDto) {
        // init
        var expected = AuthorMapper.toAuthor(authorDto);
        // method for test
        var actual = authorRepository.findById(expected.getId()).block();
        // check
        assertThat(actual).isNotNull();
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expected);
    }
}
