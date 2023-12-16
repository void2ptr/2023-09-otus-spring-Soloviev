package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.data.BooksArgumentsProvider;
import ru.otus.hw.data.CommentsArgumentsProvider;
import ru.otus.hw.data.InitTestData;
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

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbBooks = InitTestData.getDbBooks();
    }

    @DisplayName("должен загружать комментарии по id книги")
    @ParameterizedTest
    @ArgumentsSource(BooksArgumentsProvider.class)
    void shouldFindCommentByBookId(Book book) {
        // init
        var expectedComment = InitTestData.getDbComments().stream()
                .filter(comment -> comment.getBook().getId() == book.getId())
                .toList();
        // method for test
        var actualComments = commentRepository.findCommentsByBookId((book.getId()));
        // check
        assertThat(actualComments)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать комментарий по id")
    @ParameterizedTest
    @ArgumentsSource(CommentsArgumentsProvider.class)
    void shouldFindCommentByCommentId(Comment expectedComment) {
        // method for test
        var actualComment = commentRepository.findCommentById((expectedComment.getId()));
        // check
        assertThat(actualComment).isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен добавлять комментарий к книге")
    @Test
    void shouldUpdateComment() {
        // init
        long addCommentId = 0L;
        var expectedComment = new Comment(addCommentId, "The best book", dbBooks.get(0));
        // method for test
        var actualComment = commentRepository.save(expectedComment);
        // check
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
        // init
        long editCommentId = 1L;
        var expectedComment = new Comment(editCommentId, "The best book", dbBooks.get(0));
        // method for test
        var actualComment = commentRepository.save(expectedComment);
        // check
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
        // init
        long deleteCommentId = 1L;
        var comment = commentRepository.findById(deleteCommentId);
        assertThat(comment).isPresent();
        // method for test
        commentRepository.delete(comment.get());
        // check
        assertThat(commentRepository.findById(deleteCommentId)).isEmpty();
    }
}
