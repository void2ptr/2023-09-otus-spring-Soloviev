package ru.otus.hw.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.hw.data.AuthorsArgumentsProvider;
import ru.otus.hw.data.InitTestData;
import ru.otus.hw.model.mongo.MongoAuthor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе MongoDB для работы с авторами книг")
@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.hw.repositories"})
class MongoAuthorRepositoryJpaTest {

    @Autowired
    private MongoAuthorRepository authorRepository;

    private List<MongoAuthor> dbAuthors;

    @BeforeEach
    void setUp() {
        dbAuthors = InitTestData.getDbAuthors();
    }

    @DisplayName("должен загружать Aвтора по link")
    @ParameterizedTest
    @ArgumentsSource(AuthorsArgumentsProvider.class)
    void findByExternalLinkId(MongoAuthor author) {
        var expected = authorRepository.findByExternalLinkId(author.getExternalLink());
        assertThat(expected).isPresent();
        assertThat(expected.get().getFullName())
                .isEqualTo(author.getFullName());
    }

}
