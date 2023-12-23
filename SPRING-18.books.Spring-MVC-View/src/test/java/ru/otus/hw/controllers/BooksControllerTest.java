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
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.BookService;

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
class BooksControllerTest {

    private static final String BASE_URL = "/api/v1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper mapper;

    @DisplayName("проверка открытия страницы с списком Книг")
    @Test
    void listPage() throws Exception {
        // init
        String url = BASE_URL + "/book";
        Author author = new Author(1, "Author_1");
        Genre genre = new Genre(1, "Genre_1");
        List<BookDto> bookDtoList = List.of(
                new BookDto(1, "Title_1", author, List.of(genre)),
                new BookDto(2, "Title_2", author, List.of(genre)));
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
    void addPage() throws Exception {
        // init
        long bookId = 0;
        long genreId = 1;
        String url = BASE_URL + "/book/" + bookId + "/add";
        String expect = "New Book";
        List<AuthorDto> authorDtoList = List.of(
                new AuthorDto(1, "Author_1"),
                new AuthorDto(2, "Author_2"));
        Genre genre = new Genre(genreId, "Genre_1");
        given(bookService.findAllAuthorsNotInBook(bookId)).willReturn(authorDtoList);
        given(bookService.findAllGenresNotInBook(bookId)).willReturn(List.of(GenreMapper.toDto(genre)));

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
    void editPage() throws Exception {
        // init
        long bookId = 1;
        long authorId = 1;
        long genreId = 1;
        String url = BASE_URL + "/book/" + bookId + "/edit";
        String expect = "Title_1";
        Author author = new Author(authorId, "Author_1");
        Genre genre = new Genre(genreId, "Genre_1");
        BookDto bookDto = new BookDto(bookId, expect, author, List.of(genre));
        given(bookService.findById(bookId)).willReturn(Optional.of(bookDto));
        given(bookService.findAllAuthorsNotInBook(bookId)).willReturn(List.of(AuthorMapper.toDto(author)));
        given(bookService.findAllGenresNotInBook(bookId)).willReturn(List.of(GenreMapper.toDto(genre)));

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
    void deletePage() throws Exception {
        // init
        long bookId = 1;
        long authorId = 1;
        long genreId = 1;
        String url = BASE_URL + "/book/" + bookId + "/delete";
        String expect = "The Book";
        Author author = new Author(authorId, "Author_1");
        Genre genre = new Genre(genreId, "Genre_1");
        BookDto bookDto = new BookDto(bookId, expect, author, List.of(genre));
        given(bookService.findById(bookId)).willReturn(Optional.of(bookDto));
        given(bookService.findAllAuthorsNotInBook(bookId)).willReturn(List.of(AuthorMapper.toDto(author)));
        given(bookService.findAllGenresNotInBook(bookId)).willReturn(List.of(GenreMapper.toDto(genre)));
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
        long bookId = 0;
        long authorId = 1;
        long genreId = 1;
        String url = BASE_URL + "/book/" + bookId + "/add";
        String expect = "The Book";
        Author author = new Author(authorId, "Author_1");
        Genre genre = new Genre(genreId, "Genre_1");
        BookDto bookDto = new BookDto(bookId, expect, author, List.of(genre));

        given(bookService.findAuthorsById(authorId)).willReturn(AuthorMapper.toDto(author));
        given(bookService.findGenresById(genreId)).willReturn(GenreMapper.toDto(genre));

        // test
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(BookMapper.toFormUrlEncoded(bookDto))
                        .accept(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is(302))
                .andReturn();
    }

    @DisplayName("экшен редактирование Книги")
    @Test
    void editAction() throws Exception {
        // init
        long bookId = 1;
        long authorId = 1;
        long genreId = 1;
        String url = BASE_URL + "/book/" + bookId + "/edit";
        String expect = "The_Book";
        Author author = new Author(authorId, "Author_1");
        Genre genre = new Genre(genreId, "Genre_1");
        BookDto bookDto = new BookDto(bookId, expect, author, List.of(genre));
        given(bookService.findAuthorsById(authorId)).willReturn(AuthorMapper.toDto(author));
        given(bookService.findGenresById(genreId)).willReturn(GenreMapper.toDto(genre));

        // test
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(BookMapper.toFormUrlEncoded(bookDto))
                        .accept(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is(302))
                .andReturn();
    }

    @DisplayName("экшен удаления Книги")
    @Test
    void deleteAction() throws Exception {
        // init
        long bookId = 1;
        String url = BASE_URL + "/book/" + bookId + "/delete";

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