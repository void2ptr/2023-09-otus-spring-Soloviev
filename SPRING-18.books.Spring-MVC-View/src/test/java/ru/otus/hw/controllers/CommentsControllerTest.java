package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.CommentService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("проверка раздела для Комментариев")
@WebMvcTest(CommentsController.class)
class CommentsControllerTest {
    private static final String BASE_URL = "/api/v1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper mapper;

    @DisplayName("проверка открытия страницы с списком Комментариев")
    @Test
    void listPage() throws Exception {
        long bookId = 1;
        String url = BASE_URL + "/book/" + bookId + "/comment";
        BookDto bookDto = new BookDto(bookId, "Title", new AuthorDto(0, "Author"),
                List.of(new GenreDto(0, "Genre")));

        List<CommentDto> comments = new ArrayList<>();
        comments.add(new CommentDto(1, "Comment_1", bookDto));
        comments.add(new CommentDto(2, "Comment_2", bookDto));
        given(commentService.findCommentByBookId(bookId)).willReturn(comments);

        var result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(result)
                .contains("Comment_1")
                .contains("Comment_2");
    }

    @DisplayName("открытие страницы добавления Комментария")
    @Test
    void addPage() throws Exception {
        long bookId = 1;
        String expect = "New Comment";
        String url = BASE_URL + "/book/" + bookId + "/comment/page-add";
        Book book = new Book(bookId, expect, new Author(), List.of(new Genre()));

        given(commentService.findBookById(bookId)).willReturn(Optional.of(BookMapper.toDto(book)));

        String content = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(content).contains(expect);
    }

    @DisplayName("открытие страницы редактирование Комментария")
    @Test
    void editPage() throws Exception {
        long bookId = 1;
        long commentId = 1;
        String expect = "Comment 1";
        String url = BASE_URL + "/book/" + bookId + "/comment/" + commentId + "/page-edit";
        BookDto bookDto = new BookDto(bookId, "Title", new AuthorDto(0, "Author"),
                List.of(new GenreDto(0, "Genre")));
        CommentDto commentDto = new CommentDto(commentId, expect, bookDto);

        given(commentService.findBookById(bookId)).willReturn(Optional.of(bookDto));
        given(commentService.findCommentById(commentId)).willReturn(Optional.of(commentDto));

        String content = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(content).contains(expect);
    }

    @DisplayName("открытие страницы удаления Комментария")
    @Test
    void deletePage() throws Exception {
        long bookId = 1;
        long commentId = 1;
        String expect = "Comment 1";
        String url = BASE_URL + "/book/" + bookId + "/comment/" + commentId + "/page-delete";
        BookDto bookDto = new BookDto(bookId, "Title", new AuthorDto(0, "Author"),
                List.of(new GenreDto(0, "Genre")));
        CommentDto commentDto = new CommentDto(commentId, expect, bookDto);

        given(commentService.findBookById(bookId)).willReturn(Optional.of(bookDto));
        given(commentService.findCommentById(commentId)).willReturn(Optional.of(commentDto));

        String content = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(content).contains(expect);
    }

    @DisplayName("экшен добавления Комментария")
    @Test
    void insetAction() throws Exception {
        // init
        long bookId = 0;
        String expected = "Comment New";
        String url = BASE_URL + "/book/" + bookId + "/comment/add";

        // method for test
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("bookId", String.valueOf(bookId))
                        .param("description", expected)
                        .accept(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is(302))
                .andReturn();
    }

    @DisplayName("экшен редактирования Комментария")
    @Test
    void updateAction() throws Exception {
        long bookId = 1;
        long commentId = 1;
        String expected = "Comment New";
        String url = BASE_URL + "/book/" + bookId + "/comment/" + commentId + "/edit";

        // method for test
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("bookId", String.valueOf(bookId))
                        .param("commentId", String.valueOf(commentId))
                        .param("description", expected)
                        .accept(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is(302))
                .andReturn();
    }

    @DisplayName("экшен удаления Комментария")
    @Test
    void deleteAction() throws Exception {
        long bookId = 1;
        long commentId = 1;
        String url = BASE_URL + "/book/" + bookId + "/comment/" + commentId + "/delete";

        // method for test
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("bookId", String.valueOf(bookId))
                        .param("commentId", String.valueOf(commentId))
                        .accept(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is(302))
                .andReturn();
    }
}