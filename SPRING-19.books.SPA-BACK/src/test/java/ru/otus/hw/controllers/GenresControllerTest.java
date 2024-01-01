package ru.otus.hw.controllers;

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
import ru.otus.hw.data.GenresArgumentsProvider;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.Genre;
import ru.otus.hw.rest.GenresController;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("проверка раздела для Жанров")
@WebMvcTest(GenresController.class)
class GenresControllerTest {

//    private static final String BASE_URL = "/api/v1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;

    @Autowired
    private ObjectMapper mapper;

    @DisplayName("проверка получения всех с списком Жанров")
    @Test
    void getAll() throws Exception {
        String url = "/api/v1/genres";
        List<Genre> genres = List.of(
                new Genre(1, "Genre_1"),
                new Genre(2, "Genre_2"));
        given(genreService.findAll()).willReturn(genres);
        List<GenreDto> expected = genres.stream()
                .map(GenreMapper::toDto)
                .toList();

        var result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }

    @DisplayName("экшен добавления Жанра")
    @ParameterizedTest()
    @ArgumentsSource(GenresArgumentsProvider.class)
    void addAction(Genre genre) throws Exception {
        // init
        String url = "/api/v1/genres";
        given(genreService.insert(genre)).willReturn(genre);
        String expected = mapper.writeValueAsString(GenreMapper.toDto(genre));

        // method for test
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", String.valueOf(genre.getId()))
                        .content(mapper.writeValueAsBytes(genre))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @DisplayName("экшен редактирования Жанра")
    @ParameterizedTest()
    @ArgumentsSource(GenresArgumentsProvider.class)
    void updateAction(Genre genre) throws Exception {
        String url = "/api/v1/genres/" + genre.getId();
        given(genreService.update(genre)).willReturn(genre);
        String expected = mapper.writeValueAsString(GenreMapper.toDto(genre));

        // method for test
        mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", String.valueOf(genre.getId()))
                        .content(mapper.writeValueAsBytes(genre))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @DisplayName("экшен удаления Жанра")
    @ParameterizedTest()
    @ArgumentsSource(GenresArgumentsProvider.class)
    void deleteAction(Genre genre) throws Exception {
        String url = "/api/v1/genres/" + genre.getId();
        given(genreService.delete(genre.getId())).willReturn(genre);
        String expected = mapper.writeValueAsString(GenreMapper.toDto(genre));

        // method for test
        mockMvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", String.valueOf(genre.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }
}