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
import ru.otus.hw.dto.BookIdsDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.GenreRepository;
import ru.otus.hw.service.BookService;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("проверка раздела для Книги")
@WebMvcTest(BooksController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BooksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private ObjectMapper mapper;

    @DisplayName("проверка открытия страницы с списком Книг")
    @Test
//    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void listPage() throws Exception {
        // init
        AuthorDto authorDto = new AuthorDto(1, "Author_1");
        GenreDto genreDto = new GenreDto(1, "Genre_1");
        List<BookDto> bookDtoList = List.of(
                new BookDto(1, "Title_1", authorDto, List.of(genreDto)),
                new BookDto(2, "Title_2", authorDto, List.of(genreDto)));
        given(bookService.findAll()).willReturn(bookDtoList);
        // test
        var content = mockMvc.perform(get("/books"))
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
//    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void addPage() throws Exception {
        // init
        long bookId = 0;
        String expect = "New Book";
        List<AuthorDto> authorDtoList = List.of(
                new AuthorDto(1, "Author_1"),
                new AuthorDto(2, "Author_2"));
        List<Author> authors = authorRepository.findAll();
        Optional<Book> bookOpt = bookRepository.findAllById(bookId);
        given(authorRepository.findAll()).willReturn(authors);
        given(bookRepository.findAllById(bookId)).willReturn(bookOpt);

        // test
        String content = mockMvc.perform(get("/books/add"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(content).contains(expect);
    }

    @DisplayName("открытие страницы редактирования Книги")
    @Test
//    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void editPage() throws Exception {
        // init
        long bookId = 1;
        long authorId = 1;
        long genreId = 1;
        String expect = "Title_1";
        AuthorDto authorDto = new AuthorDto(authorId, "Author_1");
        GenreDto genreDto = new GenreDto(genreId, "Genre_1");
        BookDto bookDto = new BookDto(bookId, expect, authorDto, List.of(genreDto));
        given(bookService.findById(bookId)).willReturn(Optional.of(bookDto));

        // test
        String content = mockMvc.perform(get( "/books/" + bookId + "/edit")
                        .param("bookId", String.valueOf(bookId)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(content).contains(expect);
    }

    @DisplayName("открытие страницы удаления Книги")
    @Test
//    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void deletePage() throws Exception {
        // init
        long bookId = 1;
        long authorId = 1;
        long genreId = 1;
        String expect = "The Book";
        AuthorDto authorDto = new AuthorDto(authorId, "Author_1");
        GenreDto genreDto = new GenreDto(genreId, "Genre_1");
        BookDto bookDto = new BookDto(bookId, expect, authorDto, List.of(genreDto));
        given(bookService.findById(bookId)).willReturn(Optional.of(bookDto));
//        given(authorService.findAuthorsNotInBook(bookId)).willReturn(List.of(authorDto));
//        given(genreService.findGenresNotInBook(bookId)).willReturn(List.of(genreDto));
        // test
        String content = mockMvc.perform(get( "/books/" + bookId + "/delete")
                        .param("bookId", String.valueOf(bookId)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(content).contains(expect);
    }

    @DisplayName("экшен добавления Книги")
    @Test
//    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void addAction() throws Exception {
        // init
        BookIdsDto bookIdsDto = new BookIdsDto(1, "The Book", 1, List.of(1L));

        given(authorRepository.findAuthorById(1L)).willReturn(Optional.of(new Author(1L, "author")));
        given(genreRepository.findAllByIdIn(List.of(1L))).willReturn(List.of(new Genre(1L, "genre")));

        // test
        mockMvc.perform(post("/books/add")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .content(BookMapper.toFormUrlEncoded(bookIdsDto))
                        .accept(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is(302))
                .andReturn();
    }

    @DisplayName("экшен редактирование Книги")
    @Test
//    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void editAction() throws Exception {
        // init
        long bookId = 1;
        BookIdsDto bookIdsDto = new BookIdsDto(bookId, "The Book", 1, List.of(1L, 2L));
        given(authorRepository.findAuthorById(bookIdsDto.getAuthorId()))
                .willReturn(Optional.of(new Author(bookIdsDto.getAuthorId(), "author")));
        given(genreRepository.findAllByIdIn(bookIdsDto.getGenresId()))
                .willReturn(List.of(new Genre(1L, "genre"), new Genre(2L, "genre")));

        // test
        mockMvc.perform(post("/books/" + bookId + "/edit")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(BookMapper.toFormUrlEncoded(bookIdsDto))
                        .accept(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is(302))
                .andReturn();
    }

    @DisplayName("экшен удаления Книги")
    @Test
//    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void deleteAction() throws Exception {
        // init
        long bookId = 1;

        // test
        mockMvc.perform(post("/books/" + bookId + "/delete")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("id", String.valueOf(bookId))
                        .accept(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is(302))
                .andReturn();
    }
}