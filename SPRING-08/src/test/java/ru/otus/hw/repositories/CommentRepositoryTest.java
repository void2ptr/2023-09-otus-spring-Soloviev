package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.hw.AbstractInitTestData;
import ru.otus.hw.dao.InitTestData;
import ru.otus.hw.mapper.CommentsArgumentsProvider;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе MongoDB для работы с отзывами о книгах")
class CommentRepositoryTest extends AbstractInitTestData {


    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        dbAuthors = InitTestData.getAuthors();
        dbGenres = InitTestData.getDbGenres();
        dbBooks = InitTestData.getDbBooks();
        List<Comment> dbComment = InitTestData.getDbComments();
    }

    @DisplayName("должен загружать комментарий по id")
    @ParameterizedTest
    @ArgumentsSource(CommentsArgumentsProvider.class)
    void shouldFindCommentByCommentId(Comment comment) {
        // tested method
        List<Comment> comments = commentRepository.findByBookTitle(comment.getTitle());
        assertThat(comments)
                .isNotEmpty()
                .hasSize(1);
        Comment expectedComment = comments.get(0);

        // tested method
        var actualComment = commentRepository.findById((expectedComment.getId()));
        assertThat(actualComment).isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать комментарии по id книги")
    @ParameterizedTest
    @ArgumentsSource(CommentsArgumentsProvider.class)
    void shouldFindCommentByBookId(Comment comment) {
        // tested method
        List<Comment> comments = commentRepository.findByBookTitle(comment.getTitle());
        assertThat(comments).isNotEmpty().hasSize(1);
        Comment expectedComment = comments.get(0);
        // tested method
        var actualComments = commentRepository.findByBookId((expectedComment.getBook().getId()));
        assertThat(actualComments).isPresent();

        assertThat(actualComments.get())
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedComment);
    }


    @DisplayName("должен удалять комментарий")
    @Test
    void shouldDeleteComment() {
        // generate comment
        Book randomBook = bookRepository.save(this.bookRandomGenerator());
        assertThat(bookRepository.findById(randomBook.getId())).isPresent();
        Comment randomComment = commentRepository.save(this.commentRandomGenerator(randomBook));
        assertThat(commentRepository.findById(randomComment.getId())).isPresent();
        // delete comment
        commentRepository.deleteById(randomComment.getId());
        assertThat(commentRepository.findById(randomComment.getId())).isEmpty();
    }

    @DisplayName("должен добавлять заметку к комментариям")
    @Test
    void shouldInsertCommentNote() {
        String noteInsert = "Новый комментарий";
        // generate comment
        Book randomBook = bookRepository.save(this.bookRandomGenerator());
        assertThat(bookRepository.findById(randomBook.getId())).isPresent();
        Comment randomComment = commentRepository.save(this.commentRandomGenerator(randomBook));
        assertThat(commentRepository.findById(randomComment.getId())).isPresent();

        // tested method
        randomComment.addNote(noteInsert);
        // save comment
        randomComment = commentRepository.save(randomComment);
        Optional<Comment> commentAfterSave = commentRepository.findById(randomComment.getId());
        assertThat(commentAfterSave).isPresent();
        // is comment safe to BD ?
        assertThat(commentAfterSave.get().getNotes())
                .contains(noteInsert);
        // clean
        commentRepository.deleteById(commentAfterSave.get().getId());
    }


    @DisplayName("должен редактировать заметку в комментарии")
    @Test
    void shouldUpdateCommentNote() {
        String noteUpdate = "Новый комментарий на замену старому";
        // generate comment
        Book randomBook = bookRepository.save(this.bookRandomGenerator());
        assertThat(bookRepository.findById(randomBook.getId())).isPresent();
        Comment randomComment = commentRepository.save(this.commentRandomGenerator(randomBook));
        assertThat(commentRepository.findById(randomComment.getId())).isPresent();

        // tested method
        SecureRandom random = new SecureRandom();
        int noteId = random.nextInt(randomComment.getNotes().size());
        randomComment.updateNote(noteId, noteUpdate);
        // save comment
        randomComment = commentRepository.save(randomComment);
        Optional<Comment> commentAfterSave = commentRepository.findById(randomComment.getId());
        assertThat(commentAfterSave).isPresent();
        // is comment safe to BD ?
        assertThat(commentAfterSave.get().getNotes())
                .contains(noteUpdate);
        // clean
        commentRepository.deleteById(commentAfterSave.get().getId());
    }

    @DisplayName("должен удалить комментарий")
    @Test
    void shouldDeleteCommentNote() {
        // generate comment
        Book randomBook = bookRepository.save(this.bookRandomGenerator());
        assertThat(bookRepository.findById(randomBook.getId())).isPresent();
        Comment randomComment = commentRepository.save(this.commentRandomGenerator(randomBook));
        assertThat(commentRepository.findById(randomComment.getId())).isPresent();

        // tested method
        SecureRandom random = new SecureRandom();
        int noteId = random.nextInt(randomComment.getNotes().size());
        randomComment.deleteNote(noteId);
        // save comment
        randomComment = commentRepository.save(randomComment);
        Optional<Comment> commentAfterSave = commentRepository.findById(randomComment.getId());
        assertThat(commentAfterSave).isPresent();
        // is comment delete from BD ?
        assertThat(randomComment.getNotes().size() - commentAfterSave.get().getNotes().size())
                .isEqualTo(0);
        // clean
        commentRepository.deleteById(commentAfterSave.get().getId());
    }

}
