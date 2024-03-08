package ru.otus.hw.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.hw.model.BookGenre;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Репозиторий на основе R2DBC для работы с Books and Genres")
@SpringBootTest
class BookGenreRepositoryTest {

    @Autowired
    private BookGenreRepository repository;

    @DisplayName("должен удалять жанры для книги по ID книги")
    @Test
    void shouldDeleteByBookID() {
        // init
        BookGenre bookGenreFirst = repository.findAll().blockFirst();
        assertThat(bookGenreFirst).isNotNull();
        var bookGenresBefore = repository.findByBookIdIn(bookGenreFirst.getBookId()).collectList().block();
        assertThat(bookGenresBefore).isNotNull();

        var bookGenre = new BookGenre(bookGenreFirst.getBookId(), bookGenreFirst.getGenreId());
        BookGenre bookGenreSave = repository.save(bookGenre).block();
        assertThat(bookGenreSave).isNotNull();

        // test
        Mono<BookGenre> bookGenreMono = repository.deleteByBookID(bookGenreSave.getBookId());

        bookGenreMono.log().subscribe(System.out::println);
        StepVerifier
                .create(bookGenreMono)
                .expectNextCount(0)
                .verifyComplete();

        // clean
        bookGenresBefore.forEach(bg -> repository.save(new BookGenre(bg.getBookId(), bg.getGenreId())).block());

    }

    @DisplayName("должен загружать список жанров книги по ID книги")
    @Test
    void shouldFindByBookIdIn() {
        // init
        BookGenre bookGenre = repository.findAll().blockFirst();
        assertThat(bookGenre).isNotNull();
        // test
        Flux<BookGenre> bookGenreFlux = repository.findByBookIdIn(bookGenre.getBookId());
        bookGenreFlux.log().subscribe(System.out::println);
        StepVerifier
                .create(bookGenreFlux)
                .expectNextMatches(bg -> bg.getBookId().equals(bookGenre.getBookId()))
                .thenConsumeWhile(x -> true)
                .verifyComplete();
    }

    @DisplayName("должен загружать жанр принадлежащий книге по ID жанра")
    @Test
    void shouldFindByGenreId() {
        // init
        BookGenre bookGenre = repository.findAll().blockFirst();
        assertThat(bookGenre).isNotNull();
        // test
        Flux<BookGenre> bookGenreFlux = repository.findByGenreId(bookGenre.getGenreId());
        bookGenreFlux.log().subscribe(System.out::println);
        StepVerifier
                .create(bookGenreFlux)
                .expectNextMatches(bg -> bg.getGenreId().equals(bookGenre.getGenreId()))
                .thenConsumeWhile(x -> true)
                .verifyComplete();
    }

    @DisplayName("должен загружать жанр принадлежащий книге по ID жанра")
    @Test
    void shouldExistByGenreId() {
        // init
        BookGenre bookGenre = repository.findAll().blockFirst();
        assertThat(bookGenre).isNotNull();
        // test
        Mono<Boolean> booleanMono = repository.existByGenreId(bookGenre.getGenreId());
        booleanMono.log().subscribe(System.out::println);
        StepVerifier
                .create(booleanMono)
                .expectNextMatches(aBoolean -> aBoolean)
                .thenConsumeWhile(x -> true)
                .verifyComplete();
    }
}