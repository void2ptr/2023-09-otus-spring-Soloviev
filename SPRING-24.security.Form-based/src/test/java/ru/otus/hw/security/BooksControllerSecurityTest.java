package ru.otus.hw.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controller.BooksController;
import ru.otus.hw.data.UrlAndUser;
import ru.otus.hw.data.UrlBooksArgumentsProvider;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.model.Role;
import ru.otus.hw.service.AuthorService;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.GenreService;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("проверка раздела для Книг на безопасность")
@WebMvcTest(BooksController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BooksControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @DisplayName("должен открыть страницу с проверкой прав")
    @ParameterizedTest
    @ArgumentsSource(UrlBooksArgumentsProvider.class)
    void openPageOk(UrlAndUser urlAndUser) throws Exception  {

        AuthorDto authorDto = new AuthorDto(1, "Author_1");
        GenreDto genreDto = new GenreDto(1, "Genre_1");
        List<BookDto> bookDtoList = List.of(
                new BookDto(1, "Title_1", authorDto, List.of(genreDto)),
                new BookDto(2, "Title_2", authorDto, List.of(genreDto)));
        given(bookService.findAll()).willReturn(bookDtoList);

        BookDto bookDto = new BookDto(4L, "the book", authorDto, List.of(genreDto));
        given(bookService.findById(4L)).willReturn(Optional.of(bookDto));
        given(authorService.findAuthorsNotInBook(4L)).willReturn(List.of(authorDto));
        given(genreService.findGenresNotInBook(4L)).willReturn(List.of(genreDto));
        System.out.printf("----%s:%s@ %s\n", urlAndUser.user().getUsername(),
                urlAndUser.user().getPassword(), urlAndUser.url());

        // test
        mockMvc.perform(get(urlAndUser.url())
                        .with(csrf())
                        .with(user(urlAndUser.user().getUsername())
                                .password(urlAndUser.user().getPassword())
                                .roles(urlAndUser.user().getRoles().stream().map(Role::getRole).toString())))
                .andExpect(status().isOk());
    }

    @DisplayName("не должен открыть страницу")
    @ParameterizedTest
    @ArgumentsSource(UrlBooksArgumentsProvider.class)
    void openPageAnonymous(UrlAndUser urlAndUser) throws Exception  {
        // test
        mockMvc.perform(get(urlAndUser.url())
                        .with(csrf())
                        .with(anonymous()))
                .andExpect(status().is4xxClientError());
    }

}