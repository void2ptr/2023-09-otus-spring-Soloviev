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
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с книгами")
@DataJpaTest
@Import({BookRepositoryJpa.class, GenreRepositoryJpa.class})
class BookRepositoryJpaTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookRepositoryJpa bookRepository;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void shouldReturnCorrectBookById(Book expectedBook) {
        // method for test
        var actualBook = bookRepository.findBookById(expectedBook.getId());
        // check
        assertThat(actualBook).isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        // init
        List<Book> expectedBooks = dbBooks;
        // method for test
        List<Book> actualBooks = bookRepository.findAllBooks();
        // check
        assertThat(actualBooks)
                .usingRecursiveComparison()
                .isEqualTo(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        // init
        long addBookId = 0L;
        var expectedBook = new Book(addBookId, "BookTitle_10500", dbAuthors.get(0),
                List.of(dbGenres.get(0), dbGenres.get(2)));
        // method for test
        var returnedBook = bookRepository.saveBook(expectedBook);
        // check
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedBook);

        assertThat(em.find(Book.class, returnedBook.getId()))
                .usingRecursiveComparison()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        // init
        long saveBookId = 1L;
        var expectedBook = new Book(saveBookId, "BookTitle_10500", dbAuthors.get(2),
                List.of(dbGenres.get(4), dbGenres.get(5)));
        assertThat(em.find(Book.class, expectedBook.getId()))
                .isNotEqualTo(expectedBook); // проверяем отсутствие книги в ДБ

        // method for test
        var returnedBook = bookRepository.saveBook(expectedBook);
        // check
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(em.find(Book.class, returnedBook.getId()))
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id")
    @Test
    void shouldDeleteBook() {
        // init
        long deleteBookId = 1L;
        assertThat(bookRepository.findBookById(deleteBookId)).isPresent();
        // method for test
        bookRepository.deleteBookById(deleteBookId);
        // check
        assertThat(bookRepository.findBookById(deleteBookId)).isEmpty();
    }

    private static List<Author> getDbAuthors() {
        return InitTestData.getDbAuthors();
    }

    private static List<Genre> getDbGenres() {
        return InitTestData.getDbGenres();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return InitTestData.getDbBooks(dbAuthors, dbGenres);
    }

    private static List<Book> getDbBooks() {
        return InitTestData.getDbBooks();
    }
}