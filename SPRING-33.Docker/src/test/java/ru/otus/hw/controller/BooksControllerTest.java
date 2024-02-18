package ru.otus.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.data.BooksArgumentsProvider;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.model.Book;
import ru.otus.hw.service.BookService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("проверка раздела для Книги")
@WebMvcTest(BooksController.class)
class BooksControllerTest {

//    private static final String BASE_URL = "/api/v1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper mapper;

    @DisplayName("проверка возвращения списка Книг")
    @Test
    void findAll() throws Exception {
        // init
        String url = "/api/v1/books";
        AuthorDto authorDto = new AuthorDto(1, "Author_1");
        GenreDto genreDto = new GenreDto(1, "Genre_1");
        List<BookDto> expected = List.of(
                new BookDto(1, "Title_1", authorDto, List.of(genreDto)),
                new BookDto(2, "Title_2", authorDto, List.of(genreDto)));
        given(bookService.findAll()).willReturn(expected);

        // test
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }

    @DisplayName("открытие страницы добавления Книги")
    @ParameterizedTest()
    @ArgumentsSource(BooksArgumentsProvider.class)
    void addAction(Book book) throws Exception {
        // init
        BookDto bookDto = BookMapper.toDto(book);
        String url = "/api/v1/books";
        given(bookService.insert(any())).willReturn(bookDto);
        var expected = mapper.writeValueAsString(bookDto);

        // test
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expected)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @DisplayName("экшен редактирование Книги")
    @ParameterizedTest()
    @ArgumentsSource(BooksArgumentsProvider.class)
    void editAction(Book book) throws Exception {
        // init
        BookDto bookDto = BookMapper.toDto(book);
        String url = "/api/v1/books/" + bookDto.getId();
        given(bookService.update(any())).willReturn(bookDto);
        var expected = mapper.writeValueAsString(bookDto);

        // test
        mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expected)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @DisplayName("экшен удаления Книги")
    @ParameterizedTest()
    @ArgumentsSource(BooksArgumentsProvider.class)
    void deleteAction(Book book) throws Exception {
        // init
        BookDto bookDto = BookMapper.toDto(book);
        String url = "/api/v1/books/" + bookDto.getId();
        given(bookService.delete(bookDto.getId())).willReturn(bookDto);
        var expected = mapper.writeValueAsString(bookDto);

        // test
        mockMvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", String.valueOf(bookDto.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }
}
