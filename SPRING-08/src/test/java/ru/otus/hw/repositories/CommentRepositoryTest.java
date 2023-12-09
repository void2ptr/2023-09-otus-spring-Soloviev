package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.hw.AbstractInitTestData;
import ru.otus.hw.data.InitTestData;
import ru.otus.hw.data.CommentsArgumentsProvider;
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

    @DisplayName("должен загружать комментарий по id")
    @ParameterizedTest
    @ArgumentsSource(CommentsArgumentsProvider.class)
    void shouldFindCommentByCommentId(Comment comment) {
        // tested method + init
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
        comments.forEach(System.out::println);
    }

    @DisplayName("должен загружать комментарии по id книги")
    @ParameterizedTest
    @ArgumentsSource(CommentsArgumentsProvider.class)
    void shouldFindCommentByBookId(Comment comment) {
        // tested method + init
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
        Book randomBook = bookRepository.save(InitTestData.bookRandomGenerator(this.dbAuthors, this.dbGenres));
        assertThat(bookRepository.findById(randomBook.getId())).isPresent();
        Comment randomComment = commentRepository.save(InitTestData.commentRandomGenerator(randomBook));
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
        Book randomBook = bookRepository.save(InitTestData.bookRandomGenerator(this.dbAuthors, this.dbGenres));
        assertThat(bookRepository.findById(randomBook.getId())).isPresent();
        Comment randomComment = commentRepository.save(InitTestData.commentRandomGenerator(randomBook));
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
        Book randomBook = bookRepository.save(InitTestData.bookRandomGenerator(this.dbAuthors, this.dbGenres));
        assertThat(bookRepository.findById(randomBook.getId())).isPresent();
        Comment randomComment = commentRepository.save(InitTestData.commentRandomGenerator(randomBook));
        assertThat(commentRepository.findById(randomComment.getId())).isPresent();

        // tested method
        SecureRandom random = InitTestData.getSecureRandom();
        System.out.println("note: " + randomComment.getNotes().size());
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

    @DisplayName("должен удалить заметку в комментарии")
    @Test
    void shouldDeleteCommentNote() {
        // generate comment
        Book randomBook = bookRepository.save(InitTestData.bookRandomGenerator(this.dbAuthors, this.dbGenres));
        assertThat(bookRepository.findById(randomBook.getId())).isPresent();
        Comment randomComment = commentRepository.save(InitTestData.commentRandomGenerator(randomBook));
        assertThat(commentRepository.findById(randomComment.getId())).isPresent();

        // tested method
        SecureRandom random = InitTestData.getSecureRandom();
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

    @DisplayName("должен поменять 'название' книги в комментариях")
    @Test
    void shouldUpdateCommentsByBook() {
        // init - create Book and Comment
        String title = "New Book Title";
        var beforeBook= bookRepository.save(InitTestData.bookRandomGenerator(this.dbAuthors, this.dbGenres));
        commentRepository.save(InitTestData.commentRandomGenerator(beforeBook));

        // fired event - updateCommentsByBook
        beforeBook.setTitle(title);
        var afterBook = bookRepository.save(beforeBook);

        // check
        Optional<Comment> comment = commentRepository.findByBookId(afterBook.getId());
        assertThat(comment).isPresent();
        assertThat(comment.get().getTitle())
                .isEqualTo(title);
    }

    @DisplayName("должен удалить комментарий в случае удаления книги")
    @Test
    void shouldRemoveCommentsByBookId() {
        // init
        String title = "New Book Title";
        var book= bookRepository.save(InitTestData.bookRandomGenerator(this.dbAuthors, this.dbGenres));
        commentRepository.save(InitTestData.commentRandomGenerator(book));

        // fired event - removeCommentsByBookId
        bookRepository.delete(book);

        // check
        Optional<Comment> comment = commentRepository.findByBookId(book.getId());
        assertThat(comment).isEmpty();

    }
}
