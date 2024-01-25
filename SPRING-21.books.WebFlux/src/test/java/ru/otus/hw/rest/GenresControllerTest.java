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
import ru.otus.hw.data.GenresArgumentsProvider;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.service.GenreService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;



@DisplayName("Контроллер для раздела Жанров")
@WebFluxTest(GenresController.class)
class GenresControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private GenreService genreService;

    @Autowired
    private ObjectMapper mapper;

    @DisplayName("проверка получения всех с списком Жанров")
    @Test
    void getAll() {
        // init
        String url = "/api/v1/genres";
        List<GenreDto> genres = List.of(
                new GenreDto(1L, "Genre_1"),
                new GenreDto(2L, "Genre_2"));
        Flux<GenreDto> genreDtoFlux = Flux.fromStream(genres.stream());
        BDDMockito.given(genreService.findAll()).willReturn(genreDtoFlux);
        List<GenreDto> expected = genres.stream().toList();
        // test
        var actual = webClient.get().uri(url)
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GenreDto.class)
                .returnResult().getResponseBody();
        BDDMockito.then(genreService).should().findAll();

        assertThat(actual).isNotNull()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expected);
    }

    @DisplayName("экшен добавления Жанра")
    @ParameterizedTest()
    @ArgumentsSource(GenresArgumentsProvider.class)
    void addAction(GenreDto genreDto) {
        // init
        String url = "/api/v1/genres";
        Mono<GenreDto> genreDtoMono = Mono.just(genreDto);
        BDDMockito.given(genreService.insert(genreDto)).willReturn(genreDtoMono);

        // method for test
        webClient.post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(genreDto))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AuthorDto.class);

        BDDMockito.then(genreService).should().insert(any(GenreDto.class));
    }

    @DisplayName("экшен редактирования Жанра")
    @ParameterizedTest()
    @ArgumentsSource(GenresArgumentsProvider.class)
    void updateAction(GenreDto genreDto) {
        String url = "/api/v1/genres/" + genreDto.getId();
        Mono<GenreDto> genreDtoMono = Mono.just(genreDto);
        BDDMockito.given(genreService.update(genreDto)).willReturn(genreDtoMono);

        // method for test
        webClient.put().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(genreDto))
                .exchange()
                .expectStatus().isOk()
                .expectBody(GenreDto.class);

        BDDMockito.then(genreService).should().update(any(GenreDto.class));
    }

    @DisplayName("экшен удаления Жанра")
    @ParameterizedTest()
    @ArgumentsSource(GenresArgumentsProvider.class)
    void deleteAction(GenreDto genreDto) {
        String url = "/api/v1/genres/" + genreDto.getId();
        Mono<GenreDto> genreDtoMono = Mono.just(genreDto);
        BDDMockito.given(genreService.delete(genreDto.getId())).willReturn(genreDtoMono);

        // method for test
        var actual = webClient.delete().uri(url)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GenreDto.class)
                .returnResult().getResponseBody();

        BDDMockito.then(genreService).should().delete(genreDto.getId());
        assertThat(actual).isNotNull()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(genreDto);
    }
}