package ru.otus.hw.rest;

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
import ru.otus.hw.data.AuthorsArgumentsProvider;
import ru.otus.hw.dto.AuthorDto;

import ru.otus.hw.service.AuthorService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;



@DisplayName("Контроллер для раздела Авторы")
@WebFluxTest(AuthorsController.class)
public class AuthorsControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private AuthorService authorService;

    @DisplayName("проверка получения списка Авторов (Всех)")
    @Test
    void findAll() {
        // test
        String url = "/api/v1/authors";
        List<AuthorDto> authors = List.of(
                new AuthorDto(1L, "Author_1"),
                new AuthorDto(2L, "Author_2"));
        Flux<AuthorDto> authorDtoFlux = Flux.fromStream(authors.stream());
        BDDMockito.given(authorService.findAll()).willReturn(authorDtoFlux);
        List<AuthorDto> expected = authors.stream().toList();

        // test
        var actual = webClient.get().uri(url)
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AuthorDto.class)
                .returnResult().getResponseBody();

        BDDMockito.then(authorService).should().findAll();
        assertThat(actual).isNotNull()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expected);
    }

    @DisplayName("проверка получения списка Авторов (по ID)")
    @ParameterizedTest()
    @ArgumentsSource(AuthorsArgumentsProvider.class)
    void findById(AuthorDto authorDto) {
        // test
        String url = "/api/v1/authors/"+ authorDto.getId();
        Mono<AuthorDto> authorDtoMono = Mono.just(authorDto);
        BDDMockito.given(authorService.findById(authorDto.getId())).willReturn(authorDtoMono);

        // test
        var actual = webClient.get().uri(url)
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AuthorDto.class);

        BDDMockito.then(authorService).should().findById(authorDto.getId());
    }

    @DisplayName("экшен добавления Автора")
    @ParameterizedTest()
    @ArgumentsSource(AuthorsArgumentsProvider.class)
    void addAction(AuthorDto authorDto) {
        // init
        String url = "/api/v1/authors";
        Mono<AuthorDto> authorDtoMono = Mono.just(authorDto);
        BDDMockito.given(authorService.insert(authorDto)).willReturn(authorDtoMono);

        // test
        webClient.post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(authorDto))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AuthorDto.class);

        BDDMockito.then(authorService).should().insert(any(AuthorDto.class));
    }

    @DisplayName("экшен редактирование Автора")
    @ParameterizedTest()
    @ArgumentsSource(AuthorsArgumentsProvider.class)
    void updateAction(AuthorDto authorDto) {
        // init
        String url = "/api/v1/authors/" + authorDto.getId();
        Mono<AuthorDto> authorDtoMono = Mono.just(authorDto);
        given(authorService.update(authorDto)).willReturn(authorDtoMono);

        // test
        var actual = webClient.put().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(authorDto))
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthorDto.class);

        BDDMockito.then(authorService).should().update(any(AuthorDto.class));
    }

    @DisplayName("экшен удаление Автора")
    @ParameterizedTest()
    @ArgumentsSource(AuthorsArgumentsProvider.class)
    void deleteAction(AuthorDto authorDto) {
        // init
        String url = "/api/v1/authors/" + authorDto.getId();
        Mono<AuthorDto> authorDtoMono = Mono.just(authorDto);
        given(authorService.delete(authorDto.getId())).willReturn(authorDtoMono);

        // test
        var actual = webClient.delete().uri(url)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthorDto.class)
                .returnResult().getResponseBody();

        BDDMockito.then(authorService).should().delete(authorDto.getId());
        assertThat(actual).isNotNull()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(authorDto);
    }
}
