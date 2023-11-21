package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с отзывами о книгах")
@DataJpaTest
//@Import({CommentRepository.class})
//@Transactional(propagation = Propagation.NEVER)
class CommentRepositoryJpaTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private CommentRepository commentRepository;

    private List<Comment> dbComment;

    @BeforeEach
    void setUp() {
        dbComment = getDbComments();
    }

    @DisplayName("должен загружать комментарии по id книги")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void shouldFindCommentByBookId(Book book) {
        var actualComments = commentRepository.findByBookId((book.getId()));
        var expectedComment = dbComment.stream()
                .filter(comment -> comment.getBook().getId() == book.getId())
                .toList();
        assertThat(actualComments)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать комментарий по id")
    @ParameterizedTest
    @MethodSource("getDbComments")
    void shouldFindCommentByCommentId(Comment expectedComment) {
        var actualComment = commentRepository.findById((expectedComment.getId()));
        assertThat(actualComment).isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен добавлять комментарий к книге")
    @Test
    void shouldUpdateComment() {
        var expectedComment = new Comment(1L, "The best book", getDbBooks().get(0));
        var actualComment = commentRepository.save(expectedComment);
        assertThat(actualComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedComment);

        assertThat(commentRepository.findById(actualComment.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(actualComment);
    }

    @DisplayName("должен редактировать комментарий к книге")
    @Test
    void shouldInsertComment() {
        var expectedComment = new Comment(0L, "The best book", getDbBooks().get(0));
        var actualComment = commentRepository.save(expectedComment);
        assertThat(actualComment).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedComment);

        assertThat(commentRepository.findById(actualComment.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(actualComment);
    }

    @DisplayName("должен удалить комментарий")
    @Test
    void shouldDeleteComment() {
        var comment = commentRepository.findById(1L);
        assertThat(comment).isPresent();
        commentRepository.delete(comment.get());
        assertThat(commentRepository.findById(1L)).isEmpty();
    }

    private static List<Comment> getDbComments() {
        return InitTestData.getDbComments();
    }

    private static List<Book> getDbBooks() {
        return InitTestData.getDbBooks();
    }
}
