package ru.otus.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;
import ru.otus.hw.service.CommentService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("проверка раздела для Комментариев")
@WebMvcTest(CommentsController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper mapper;

    @DisplayName("проверка открытия страницы с списком Комментариев")
    @Test
//    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void listPage() throws Exception {
        long bookId = 1;
        String url = "/books/" + bookId + "/comments";
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
//    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void addPage() throws Exception {
        long bookId = 1;
        String expect = "The Book";
        String url = "/books/" + bookId + "/comments/add";
        Book book = new Book(bookId, expect, new Author(0, "The Author"),
                List.of(new Genre(0, "New Comment")));
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
//    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void editPage() throws Exception {
        long bookId = 1;
        long commentId = 1;
        String expect = "Comment 1";
        String url = "/books/" + bookId + "/comments/" + commentId + "/edit";
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
//    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void deletePage() throws Exception {
        long bookId = 1;
        long commentId = 1;
        String expect = "Comment 1";
        String url = "/books/" + bookId + "/comments/" + commentId + "/delete";
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
//    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void insetAction() throws Exception {
        // init
        long bookId = 0;
        String expected = "Comment New";
        String url = "/books/" + bookId + "/comments/add";

        // method for test
        mockMvc.perform(post(url)
                        .with(csrf())
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
//    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void updateAction() throws Exception {
        long bookId = 1;
        long commentId = 1;
        String expected = "Comment New";
        String url = "/books/" + bookId + "/comments/" + commentId + "/edit";

        // method for test
        mockMvc.perform(post(url)
                        .with(csrf())
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
//    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void deleteAction() throws Exception {
        long bookId = 1;
        long commentId = 1;
        String url = "/books/" + bookId + "/comments/" + commentId + "/delete";

        // method for test
        mockMvc.perform(post(url)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("bookId", String.valueOf(bookId))
                        .param("commentId", String.valueOf(commentId))
                        .accept(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is(302))
                .andReturn();
    }
}