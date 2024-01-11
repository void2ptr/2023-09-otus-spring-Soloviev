package ru.otus.hw.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.otus.hw.data.BooksArgumentsProvider;
import ru.otus.hw.data.InitTestData;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.model.*;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе R2DBC для работы с книгами")
@SpringBootTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    private List<AuthorDto> dbAuthors;

    private List<BookDto> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = InitTestData.getDbAuthors();
        List<GenreDto> dbGenres = InitTestData.getDbGenres();
        dbBooks = InitTestData.getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @ArgumentsSource(BooksArgumentsProvider.class)
    void shouldReturnCorrectBookById(BookDto expected) {
        // method for test
        var actual = bookRepository.findById(expected.getId()).block();
        // check
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expected);
    }

    @DisplayName("должен загружать книги по AUTHOR_ID (StepVerifier)")
    @ParameterizedTest
    @ArgumentsSource(BooksArgumentsProvider.class)
    void shouldFindByAuthorId(BookDto expected) {
        // test
        Flux<Book> bookFlux = bookRepository.findByAuthorId(expected.getAuthor().getId());
        bookFlux.log().subscribe(System.out::println);
        StepVerifier
                .create(bookFlux)
                .expectNextMatches(book -> book.getAuthorId().equals(expected.getAuthor().getId()))
                .thenConsumeWhile(x -> true)
                .verifyComplete();
    }



    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        // init
        List<Book> expectedBooks = dbBooks.stream()
                .map(BookMapper::toBook)
                .toList();
        // method for test
        List<Book> actualBooks = bookRepository.findAll()
                .sort(Comparator.comparing(Book::getId))
                .collectList()
                .block();
        // check
        assertThat(actualBooks)
                .usingRecursiveComparison()
                .isEqualTo(expectedBooks);
//        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        // init
        var expected = new Book("New Book", dbAuthors.get(0).getId());
        // method for test
        var actual = bookRepository.save(expected).block();
        // check
        assertThat(actual).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expected);

        assertThat(bookRepository.findById(actual.getId()).block())
                .usingRecursiveComparison()
                .isEqualTo(expected);
        // clean
        bookRepository.delete(expected).block();
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        // init
        long editBookId = 1L;
        var before = bookRepository.findById(editBookId).block();
        var expected = new Book(editBookId, "Update Book", dbAuthors.get(2).getId());
        assertThat(before).isNotNull();

        // method for test
        var actual = bookRepository.save(expected).block();
        // check
        assertThat(actual)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expected);

        assertThat(bookRepository.findById(actual.getId()).block())
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(actual);
        // clean
        bookRepository.save(before).block();
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        // init
        Book before = bookRepository.findAll().blockFirst();
        assertThat(before).isNotNull();
        Book actual = bookRepository.save(new Book("DELETE Book test", before.getAuthorId())).block();
        assertThat(actual).isNotNull();
        // method for test
        bookRepository.deleteById(actual.getId()).block();
        // check
        assertThat(bookRepository.findById(actual.getId()).block()).isNull();
    }
}
