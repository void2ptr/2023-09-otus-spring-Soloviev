package ru.otus.hw.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.hw.data.BooksArgumentsProvider;
import ru.otus.hw.data.CommentsArgumentsProvider;
import ru.otus.hw.data.InitTestData;
import ru.otus.hw.model.mongo.MongoBook;
import ru.otus.hw.model.mongo.MongoComment;
import ru.otus.hw.model.postgres.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе MongoDB для работы с отзывами о книгах")
@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.hw.repositories"})
class MongoCommentRepositoryJpaTest {


    @Autowired
    private MongoCommentRepository commentRepository;

    private List<MongoBook> dbBooks;

    @BeforeEach
    void setUp() {
        List<MongoComment> dbComment = InitTestData.getDbComments();
        dbBooks = InitTestData.getDbBooks();
    }

    @DisplayName("должен загружать комментарии по id книги")
    @ParameterizedTest
    @ArgumentsSource(BooksArgumentsProvider.class)
    void findByExternalLinkId(MongoBook author) {
//        var expected = commentRepository.findByExternalLinkId(author.getExternalLink());
//        assertThat(expected).isPresent();
//        assertThat(expected.get().getTitle())
//                .isEqualTo(author.getTitle());
    }

}
