package ru.otus.hw.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.hw.data.BooksArgumentsProvider;
import ru.otus.hw.data.CommentsArgumentsProvider;
import ru.otus.hw.data.InitTestData;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.mapper.CommentMapper;
import ru.otus.hw.model.Comment;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе R2DBC для работы с отзывами о книгах")
@SpringBootTest
//@AutoConfigureTestDatabase
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        List<BookDto> dbBooks = InitTestData.getDbBooks();
        List<CommentDto> dbComment = InitTestData.getDbComments();
    }

    @DisplayName("должен загружать комментарии по id книги")
    @ParameterizedTest
    @ArgumentsSource(BooksArgumentsProvider.class)
    void shouldFindCommentByBookId(BookDto bookDto) {
        // init
        var expected = commentRepository.findAll()
                .filter(comment -> comment.getBookId().equals(bookDto.getId()))
                .sort(Comparator.comparing(Comment::getId))
                .collectList()
                .block();
        // method for test
        var actual = commentRepository.findCommentsByBookId((bookDto.getId()))
                .sort(Comparator.comparing(Comment::getId))
                .collectList()
                .block();
        // check
        assertThat(actual)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expected);
    }

    @DisplayName("должен загружать комментарии по id книги (StepVerifier)")
    @ParameterizedTest
    @ArgumentsSource(BooksArgumentsProvider.class)
    void shouldFindCommentByBookIdReact(BookDto bookDto) {

        // method for test
        Flux<Comment> commentFlux = commentRepository.findCommentsByBookId(bookDto.getId());
        commentFlux.log().subscribe(System.out::println);
        StepVerifier
                .create(commentFlux)
                .expectNextMatches(comment -> comment.getBookId().equals(bookDto.getId()))
                .thenConsumeWhile(x -> true)
                .verifyComplete();
    }

    @DisplayName("должен загружать комментарий по id")
    @ParameterizedTest
    @ArgumentsSource(BooksArgumentsProvider.class)
    void shouldFindCommentByCommentId(BookDto bookDto) {
        // init
        var expected = commentRepository.findCommentsByBookId(bookDto.getId()).blockFirst();
        assertThat(expected).isNotNull();

        // method for test
        var actual = commentRepository.findCommentById((expected.getId()))
                .block();
        // check
        assertThat(actual)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expected);
    }

    @DisplayName("должен загружать комментарий по id (StepVerifier)")
    @Test
    void shouldFindCommentById() {
        var expected = commentRepository.findAll().blockFirst();
        assertThat(expected).isNotNull();

        // method for test
        Mono<Comment> commentFlux = commentRepository.findCommentById(expected.getId());
        commentFlux.log().subscribe(System.out::println);
        StepVerifier
                .create(commentFlux)
                .expectNextMatches(comment -> comment.getId().equals(expected.getId()))
                .expectComplete()
                .verify();
    }

    @DisplayName("должен добавлять комментарий к книге")
    @ParameterizedTest
    @ArgumentsSource(BooksArgumentsProvider.class)
    void shouldUpdateComment(BookDto bookDto) {
        // init
        var before = commentRepository.findCommentsByBookId(bookDto.getId()).blockFirst();
        assertThat(before).isNotNull();
        var expected = new Comment(before.getId(), before.getBookId(), "The UPDATE book comment test");

        // method for test
        var actual = commentRepository.save(expected).block();
        // check
        assertThat(actual)
                .isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expected);
        assertThat(commentRepository.findById(actual.getId()).block())
                .usingRecursiveComparison()
                .isEqualTo(actual);
        // clean
        commentRepository.save(before).block();
    }

    @DisplayName("должен редактировать комментарий к книге")
    @ParameterizedTest
    @ArgumentsSource(BooksArgumentsProvider.class)
    void shouldInsertComment(BookDto bookDto) {
        // init
        var expected = new Comment(bookDto.getId(), "The INSERT book test");
        // method for test
        var actual = commentRepository.save(expected).block();
        // check
        assertThat(actual)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expected);
        assertThat(commentRepository.findById(actual.getId()).block())
                .usingRecursiveComparison()
                .isEqualTo(actual);
        // clean
        commentRepository.delete(actual).block();
    }

    @DisplayName("должен удалить комментарий")
    @ParameterizedTest
    @ArgumentsSource(CommentsArgumentsProvider.class)
    void shouldDeleteComment(CommentDto commentDto) {
        // init
        var before = CommentMapper.toComment(commentDto);
        // method for test
        commentRepository.delete(before).block();
        // check
        assertThat(commentRepository.findById(before.getId()).block()).isNull();
        // clean (restore)
        commentRepository.save(new Comment(before.getBookId(), before.getDescription())).block();
    }

}
