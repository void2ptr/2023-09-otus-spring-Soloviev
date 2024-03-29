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
import ru.otus.hw.data.GenresArgumentsProvider;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.model.Genre;
import ru.otus.hw.service.GenreService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("проверка раздела для Жанров")
@WebMvcTest(GenresController.class)
class GenresControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;

    @Autowired
    private ObjectMapper mapper;

    @DisplayName("проверка получения всех с списком Жанров")
    @Test
    void getAll() throws Exception {
        List<GenreDto> expected = List.of(
                new GenreDto(1, "Genre_1"),
                new GenreDto(2, "Genre_2"));
        given(genreService.findAll()).willReturn(expected);

        mockMvc.perform(get("/api/v1/genres"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }

    @DisplayName("экшен добавления Жанра")
    @ParameterizedTest()
    @ArgumentsSource(GenresArgumentsProvider.class)
    void addAction(Genre genre) throws Exception {
        GenreDto genreDto = GenreMapper.toDto(genre);
        // init
        given(genreService.insert(any())).willReturn(genreDto);
        String expected = mapper.writeValueAsString(genreDto);

        // method for test
        mockMvc.perform(post("/api/v1/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expected)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @DisplayName("экшен редактирования Жанра")
    @ParameterizedTest()
    @ArgumentsSource(GenresArgumentsProvider.class)
    void updateAction(Genre genre) throws Exception {
        GenreDto genreDto = GenreMapper.toDto(genre);
        given(genreService.update(any())).willReturn(genreDto);
        String expected = mapper.writeValueAsString(genreDto);

        // method for test
        mockMvc.perform(put("/api/v1/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expected)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @DisplayName("экшен удаления Жанра")
    @ParameterizedTest()
    @ArgumentsSource(GenresArgumentsProvider.class)
    void deleteAction(Genre genre) throws Exception {
        GenreDto genreDto = GenreMapper.toDto(genre);
        given(genreService.delete(genre.getId())).willReturn(genreDto);
        String expected = mapper.writeValueAsString(genreDto);

        // method for test
        mockMvc.perform(delete("/api/v1/genres/" + genre.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", String.valueOf(genre.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }
}