package ru.otus.hw.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
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
import ru.otus.hw.data.CommentsArgumentsProvider;

import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.service.CommentService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;



@DisplayName("Контроллер для раздела Комментарии")
@WebFluxTest(CommentsController.class)
class CommentsControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper mapper;

    @DisplayName("проверка получения списка Комментариев")
    @ParameterizedTest()
    @ArgumentsSource(CommentsArgumentsProvider.class)
    void shouldReturnCommentsForBook(CommentDto commentDto) {

        String url = "/api/v1/books/" + commentDto.getBook().getId() + "/comments";

        List<CommentDto> expected = new ArrayList<>();
        expected.add(new CommentDto(1L, "Comment_1", commentDto.getBook()));
        expected.add(new CommentDto(2L, "Comment_2", commentDto.getBook()));
        Flux<CommentDto> genreDtoFlux = Flux.fromStream(expected.stream());
        given(commentService.findAllByBookId(commentDto.getBook().getId())).willReturn(genreDtoFlux);
        // test
        var actual = webClient.get().uri(url)
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CommentDto.class)
                .returnResult().getResponseBody();
        BDDMockito.then(commentService).should().findAllByBookId(commentDto.getBook().getId());

        assertThat(actual).isNotNull()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expected);
    }

    @DisplayName("экшен добавления Комментария")
    @ParameterizedTest()
    @ArgumentsSource(CommentsArgumentsProvider.class)
    void insetAction(CommentDto commentDto) {
        // init
        String url = "/api/v1/books/" + commentDto.getBook().getId() + "/comments";
        Mono<CommentDto> commentDtoMono = Mono.just(commentDto);
        given(commentService.insert(any())).willReturn(commentDtoMono);

        // method for test
        webClient.post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(commentDto))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CommentDto.class);

        BDDMockito.then(commentService).should().insert(any(CommentDto.class));
    }

    @DisplayName("экшен редактирования Комментария")
    @ParameterizedTest()
    @ArgumentsSource(CommentsArgumentsProvider.class)
    void updateAction(CommentDto commentDto) {
        String url = "/api/v1/books/" + commentDto.getBook().getId() + "/comments/" + commentDto.getId();
        Mono<CommentDto> commentDtoMono = Mono.just(commentDto);
        given(commentService.update(commentDto)).willReturn(commentDtoMono);

        // method for test
        webClient.put().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(commentDto))
                .exchange()
                .expectStatus().isOk()
                .expectBody(CommentDto.class);

        BDDMockito.then(commentService).should().update(any(CommentDto.class));
    }

    @DisplayName("экшен удаления Комментария")
    @ParameterizedTest()
    @ArgumentsSource(CommentsArgumentsProvider.class)
    void deleteAction(CommentDto commentDto) {
        String url = "/api/v1/books/" + commentDto.getBook().getId() + "/comments/" + commentDto.getId();
        Mono<CommentDto> commentDtoMono = Mono.just(commentDto);
        given(commentService.delete(commentDto.getId())).willReturn(commentDtoMono);

        // method for test
        var actual = webClient.delete().uri(url)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CommentDto.class)
                .returnResult().getResponseBody();
        assertThat(actual).isNotNull()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(commentDto);
    }

}