package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.hw.AbstractInitTestData;
import ru.otus.hw.mapper.BooksArgumentsProvider;
import ru.otus.hw.dao.InitTestData;
import ru.otus.hw.models.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе MongoDB для работы с книгами")
class BookRepositoryTest extends AbstractInitTestData {

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        dbAuthors = InitTestData.getAuthors();
        dbGenres = InitTestData.getDbGenres();
        dbBooks = InitTestData.getDbBooks();
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        // tested method
        List<Book> actualBooks = bookRepository.findAll();
        List<Book> expectedBooks = dbBooks;
        // clean _id
        actualBooks = actualBooks.stream()
                .map(this::cleanId)
                .collect(Collectors.toList());
        // sync List
        List<Book> retrainBooks = new ArrayList<>(expectedBooks);
        retrainBooks.containsAll(actualBooks);

        assertThat(retrainBooks)
                .usingRecursiveComparison()
                .isEqualTo(expectedBooks);
        actualBooks.forEach(System.out::println);
    }


    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @ArgumentsSource(BooksArgumentsProvider.class)
    void shouldReturnCorrectBookById(Book expectedBook) {
        // tested method
        Optional<Book> bookByTitle = bookRepository.findByTitleIs(expectedBook.getTitle());
        assertThat(bookByTitle)
                .isPresent();

        // tested method
        var actualBook = bookRepository.findById(bookByTitle.get().getId());
        assertThat(actualBook)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать книгу по названию")
    @ParameterizedTest
    @ArgumentsSource(BooksArgumentsProvider.class)
    void shouldReturnCorrectBooksByTitle(Book expectedBook) {
        // tested method
        List<Book> bookByTitle = bookRepository.findAllByTitleIs(expectedBook.getTitle());
        assertThat(bookByTitle)
                .isNotEmpty()
                .map(this::cleanId)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(expectedBook));

        // tested method
        bookByTitle.forEach(book -> {
                    var actualBook = bookRepository.findAllByTitleIs(book.getTitle());
                    assertThat(actualBook)
                            .map(this::cleanId)
                            .usingRecursiveComparison()
                            .ignoringExpectedNullFields()
                            .isEqualTo(List.of(book));
                });
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var expectedBook = this.bookRandomGenerator();
        // tested method
        var actualBook = bookRepository.save(expectedBook);
        assertThat(actualBook)
                .isNotNull()
                .matches(book -> book.getTitle().equals(expectedBook.getTitle()))
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedBook);

        assertThat(bookRepository.findById(actualBook.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(actualBook);
        // clean
        bookRepository.delete(actualBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var randomBook = this.bookRandomGenerator();
        // tested method
        var expectedBook = bookRepository.save(randomBook);

        assertThat(expectedBook).isNotNull()
                .matches(book -> ! book.getId().isEmpty())
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(randomBook);

        assertThat(bookRepository.findById(expectedBook.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
        // clean
        bookRepository.delete(expectedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        var expectedBook = bookRepository.save(this.bookRandomGenerator());
        assertThat(bookRepository.findById(expectedBook.getId())).isPresent();
        // tested method
        bookRepository.delete(expectedBook);
        assertThat(bookRepository.findById(expectedBook.getId())).isEmpty();
    }

    private Book cleanId(Book book) {
        book.setId(null);
        book.getAuthors().forEach(author -> author.setId(null));
        book.getGenres().forEach(genre -> genre.setId(null));
        return book;
    }
}
