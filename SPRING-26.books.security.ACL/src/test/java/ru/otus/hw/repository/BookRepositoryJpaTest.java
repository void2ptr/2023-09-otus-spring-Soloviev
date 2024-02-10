package ru.otus.hw.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.data.BooksArgumentsProvider;
import ru.otus.hw.data.InitTestData;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с книгами")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryJpaTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookRepository bookRepository;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = InitTestData.getDbAuthors();
        dbGenres = InitTestData.getDbGenres();
        dbBooks = InitTestData.getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @ArgumentsSource(BooksArgumentsProvider.class)
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
        List<Book> actualBooks = bookRepository.findAll();
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
        var returnedBook = bookRepository.save(expectedBook);
        expectedBook.setId(returnedBook.getId());
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
        long editBookId = 1L;
        var expectedBook = new Book(editBookId, "BookTitle_10500", dbAuthors.get(2),
                List.of(dbGenres.get(4), dbGenres.get(5)));
        assertThat(em.find(Book.class,expectedBook.getId()))
                .isNotEqualTo(expectedBook); // проверить отсутствие

        // method for test
        var returnedBook = bookRepository.save(expectedBook);
        // check
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(em.find(Book.class, returnedBook.getId()))
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        // init
        long deleteBookId = 1L;
        assertThat(em.find(Book.class, deleteBookId)).isNotNull();
        // method for test
        bookRepository.deleteById(deleteBookId);
        // check
        assertThat(em.find(Book.class, deleteBookId)).isNull();
    }
}
