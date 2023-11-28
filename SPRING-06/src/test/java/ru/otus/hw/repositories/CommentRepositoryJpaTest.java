package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с отзывами о книгах")
@DataJpaTest
@Import({CommentRepositoryJpa.class})
//@Transactional(propagation = Propagation.NEVER)
class CommentRepositoryJpaTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private CommentRepositoryJpa commentRepository;

    private List<Comment> dbComment;

    @BeforeEach
    void setUp() {
        dbComment = getDbComments();
    }

    @DisplayName("должен загружать комментарии по id книги")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void shouldFindCommentByBookId(Book book) {
        // tested method
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
        // method for test
        Optional<Comment> actualComment = commentRepository.findById(expectedComment.getId());

        assertThat(actualComment)
                .isPresent().get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен добавлять комментарий к книге")
    @Test
    void shouldUpdateComment() {
        long addCommentID = 0l;
        var expectedComment = new Comment(addCommentID, "The best book", getDbBooks().get(0));
        // method for test
        var actualComment = commentRepository.save(expectedComment);

        assertThat(actualComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedComment);

        assertThat(em.find(Comment.class, actualComment.getId()))
                .usingRecursiveComparison()
                .isEqualTo(actualComment);
    }

    @DisplayName("должен редактировать комментарий к книге")
    @Test
    void shouldInsertComment() {
        long editCommentID = 1l;
        var expectedComment = new Comment(editCommentID, "The best book", getDbBooks().get(0));
        // method for test
        var actualComment = commentRepository.save(expectedComment);

        assertThat(actualComment).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedComment);

        assertThat(em.find(Comment.class, actualComment.getId()))
                .usingRecursiveComparison()
                .isEqualTo(actualComment);
    }

    @DisplayName("должен удалить комментарий")
    @Test
    void shouldDeleteComment() {
        long deleteCommentID = 1l;
        Comment comment = em.find(Comment.class, deleteCommentID);
        assertThat(comment).isNotNull();
        // method for test
        commentRepository.delete(comment);
        assertThat(em.find(Comment.class, deleteCommentID)).isNull();
    }

    private static List<Comment> getDbComments() {
        return InitTestData.getDbComments();
    }

    private static List<Book> getDbBooks() {
        return InitTestData.getDbBooks();
    }
}