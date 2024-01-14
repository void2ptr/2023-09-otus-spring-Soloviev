package ru.otus.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookIdsDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.model.Genre;
import ru.otus.hw.service.AuthorService;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.GenreService;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("проверка раздела для Книги")
@WebMvcTest(BooksController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BooksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @Autowired
    private ObjectMapper mapper;

    @DisplayName("проверка открытия страницы с списком Книг")
    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void listPage() throws Exception {
        // init
        String url = "/books";
        AuthorDto authorDto = new AuthorDto(1, "Author_1");
        GenreDto genreDto = new GenreDto(1, "Genre_1");
        List<BookDto> bookDtoList = List.of(
                new BookDto(1, "Title_1", authorDto, List.of(genreDto)),
                new BookDto(2, "Title_2", authorDto, List.of(genreDto)));
        given(bookService.findAll()).willReturn(bookDtoList);
        // test
        var content = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(content)
                .contains("Title_1")
                .contains("Title_2")
                .contains("Author_1")
                .contains("Genre_1");
    }

    @DisplayName("открытие страницы добавления Книги")
    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void addPage() throws Exception {
        // init
        long bookId = 0;
        long genreId = 1;
        String url = "/books/add";
        String expect = "New Book";
        List<AuthorDto> authorDtoList = List.of(
                new AuthorDto(1, "Author_1"),
                new AuthorDto(2, "Author_2"));
        Genre genre = new Genre(genreId, "Genre_1");
        given(authorService.findAuthorsNotInBook(bookId)).willReturn(authorDtoList);
        given(genreService.findGenresNotInBook(bookId)).willReturn(List.of(GenreMapper.toDto(genre)));

        // test
        String content = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(content).contains(expect);
    }

    @DisplayName("открытие страницы редактирования Книги")
    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void editPage() throws Exception {
        // init
        long bookId = 1;
        long authorId = 1;
        long genreId = 1;
        String url = "/books/" + bookId + "/edit";
        String expect = "Title_1";
        AuthorDto authorDto = new AuthorDto(authorId, "Author_1");
        GenreDto genreDto = new GenreDto(genreId, "Genre_1");
        BookDto bookDto = new BookDto(bookId, expect, authorDto, List.of(genreDto));
        given(bookService.findById(bookId)).willReturn(Optional.of(bookDto));
        given(authorService.findAuthorsNotInBook(bookId)).willReturn(List.of(authorDto));
        given(genreService.findGenresNotInBook(bookId)).willReturn(List.of(genreDto));

        // test
        String content = mockMvc.perform(get(url)
                        .param("bookId", String.valueOf(bookId)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(content).contains(expect);
    }

    @DisplayName("открытие страницы удаления Книги")
    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void deletePage() throws Exception {
        // init
        long bookId = 1;
        long authorId = 1;
        long genreId = 1;
        String url = "/books/" + bookId + "/delete";
        String expect = "The Book";
        AuthorDto authorDto = new AuthorDto(authorId, "Author_1");
        GenreDto genreDto = new GenreDto(genreId, "Genre_1");
        BookDto bookDto = new BookDto(bookId, expect, authorDto, List.of(genreDto));
        given(bookService.findById(bookId)).willReturn(Optional.of(bookDto));
        given(authorService.findAuthorsNotInBook(bookId)).willReturn(List.of(authorDto));
        given(genreService.findGenresNotInBook(bookId)).willReturn(List.of(genreDto));
        // test
        String content = mockMvc.perform(get(url)
                        .param("bookId", String.valueOf(bookId)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(content).contains(expect);
    }

    @DisplayName("экшен добавления Книги")
    @Test
    void addAction() throws Exception {
        // init
        String url = "/books/add";
        BookIdsDto bookIdsDto = new BookIdsDto(1, "The Book", 1, List.of(1L));

        // test
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .content(BookMapper.toFormUrlEncoded(bookIdsDto))
                        .accept(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is(302))
                .andReturn();
    }

    @DisplayName("экшен редактирование Книги")
    @Test
    void editAction() throws Exception {
        // init
        long bookId = 1;
        String url = "/books/" + bookId + "/edit";
        BookIdsDto bookIdsDto = new BookIdsDto(bookId, "The Book", 1, List.of(1L, 2L));

        // test
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(BookMapper.toFormUrlEncoded(bookIdsDto))
                        .accept(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is(302))
                .andReturn();
    }

    @DisplayName("экшен удаления Книги")
    @Test
    void deleteAction() throws Exception {
        // init
        long bookId = 1;
        String url = "/books/" + bookId + "/delete";

        // test
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("id", String.valueOf(bookId))
                        .accept(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is(302))
                .andReturn();
    }
}