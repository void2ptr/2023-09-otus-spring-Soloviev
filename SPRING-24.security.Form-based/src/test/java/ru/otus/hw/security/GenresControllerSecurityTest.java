package ru.otus.hw.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controller.GenresController;
import ru.otus.hw.data.UrlAndUser;
import ru.otus.hw.data.UrlGenresArgumentsProvider;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.model.Role;
import ru.otus.hw.service.GenreService;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("проверка раздела для Жанры на безопасность")
@WebMvcTest(GenresController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GenresControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;

    @DisplayName("должен открыть страницу с проверкой прав")
    @ParameterizedTest
    @ArgumentsSource(UrlGenresArgumentsProvider.class)
    void openPageOk(UrlAndUser urlAndUser) throws Exception  {

        List<GenreDto> genres = List.of(
                new GenreDto(1, "Genre_1"),
                new GenreDto(2, "Genre_2"));
        given(genreService.findAll()).willReturn(genres);
        GenreDto genreDto = new GenreDto(4L, "Genre XXX");
        given(genreService.findGenreById(4L)).willReturn(Optional.of(genreDto));

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
    @ArgumentsSource(UrlGenresArgumentsProvider.class)
    void openPageAnonymous(UrlAndUser urlAndUser) throws Exception  {
        // test
        mockMvc.perform(get(urlAndUser.url())
                        .with(csrf())
                        .with(anonymous()))
                .andExpect(status().is4xxClientError());
    }

}