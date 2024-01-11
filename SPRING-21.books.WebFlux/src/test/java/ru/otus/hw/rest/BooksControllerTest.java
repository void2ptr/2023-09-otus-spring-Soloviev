package ru.otus.hw.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.data.BooksArgumentsProvider;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.service.BookService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@DisplayName("проверка раздела для Книги")
@WebFluxTest(BooksController.class)
class BooksControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper mapper;

    @DisplayName("проверка возвращения списка Книг")
    @Test
    void findAll() {
        // init
        String url = "/api/v1/books";
        AuthorDto authorDto = new AuthorDto(1L, "Author_1");
        GenreDto genreDto = new GenreDto(1L, "Genre_1");
        List<BookDto> expected = List.of(
                new BookDto(1L, "Title_1", authorDto, List.of(genreDto)),
                new BookDto(2L, "Title_2", authorDto, List.of(genreDto)));
        Flux<BookDto> bookDtoFlux = Flux.fromStream(expected.stream());
        BDDMockito.given(bookService.findAll()).willReturn(bookDtoFlux);

        // test
        var actual = webClient.get().uri(url)
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AuthorDto.class)
                .returnResult().getResponseBody();

        BDDMockito.then(bookService).should().findAll();
        assertThat(actual).isNotNull()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expected);
    }

    @DisplayName("открытие страницы добавления Книги")
    @ParameterizedTest()
    @ArgumentsSource(BooksArgumentsProvider.class)
    void addAction(BookDto bookDto) {
        // init
        String url = "/api/v1/books";
        Mono<BookDto> bookDtoMono = Mono.just(bookDto);
        given(bookService.insert(any())).willReturn(bookDtoMono);

        // test
        webClient.post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(bookDto))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BookDto.class);

        BDDMockito.then(bookService).should().insert(any(BookDto.class));
    }

    @DisplayName("экшен редактирование Книги")
    @ParameterizedTest()
    @ArgumentsSource(BooksArgumentsProvider.class)
    void editAction(BookDto bookDto) {
        // init
        String url = "/api/v1/books/" + bookDto.getId();
        Mono<BookDto> bookDtoMono = Mono.just(bookDto);
        given(bookService.update(bookDto)).willReturn(bookDtoMono);

        // test
        webClient.put().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(bookDto))
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class);

        BDDMockito.then(bookService).should().update(any(BookDto.class));
    }

    @DisplayName("экшен удаления Книги")
    @ParameterizedTest()
    @ArgumentsSource(BooksArgumentsProvider.class)
    void deleteAction(BookDto bookDto) {
        // init
        String url = "/api/v1/books/" + bookDto.getId();
        Mono<BookDto> bookDtoMono = Mono.just(bookDto);
        given(bookService.delete(bookDto.getId())).willReturn(bookDtoMono);

        // test
        var actual = webClient.delete().uri(url)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthorDto.class)
                .returnResult().getResponseBody();

        BDDMockito.then(bookService).should().delete(bookDto.getId());
        assertThat(actual).isNotNull()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(bookDto);
    }
}
