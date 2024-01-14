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
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.service.GenreService;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("проверка раздела для Жанров")
@WebMvcTest(GenresController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GenresControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;


    @Autowired
    private ObjectMapper mapper;


    @DisplayName("проверка открытия страницы с списком Жанров")
    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void listPage() throws Exception {
        String url = "/genres";
        List<GenreDto> genres = List.of(
                new GenreDto(1, "Genre_1"),
                new GenreDto(2, "Genre_2"));
        given(genreService.findAll()).willReturn(genres);

        var result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(result)
                .contains("Genre_1")
                .contains("Genre_2");
    }

    @DisplayName("открытие страницы добавления Жанра")
    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void addPage() throws Exception {
        String url = "/genres/add";
        String expect = "new Genre";

        String content = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(content).contains(expect);
    }

    @DisplayName("открытие страницы редактирования Жанра")
    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void editPage() throws Exception {
        long id = 1;
        String url = "/genres/" + id + "/edit";
        String expect = "Genre_EDIT";
        GenreDto genreDto = new GenreDto(id, expect);
        given(genreService.findGenreById(id)).willReturn(Optional.of(genreDto));

        String content = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(content).contains(expect);
    }

    @DisplayName("открытие страницы удаления Жанра")
    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void deletePage() throws Exception {
        long id = 1;
        String expect = "Genre_DELETE";
        String url = "/genres/" + id + "/delete";

        GenreDto genreDto = new GenreDto(id, expect);
        given(genreService.findGenreById(id)).willReturn(Optional.of(genreDto));

        String content = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(content).contains(expect);
    }

    @DisplayName("экшен добавления Жанра")
    @Test
    void addAction() throws Exception {
        // init
        String url = "/genres/add";
        GenreDto genreDto = new GenreDto(0, "Author_NEW");

        // method for test
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("id", String.valueOf(genreDto.getId()))
                        .param("name", genreDto.getName())
                        .accept(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is(302))
                .andReturn();
    }

    @DisplayName("экшен редактирования Жанра")
    @Test
    void updateAction() throws Exception {
        long id = 1;
        GenreDto genreDto = new GenreDto(id, "Genre_NEW");
        String url = "/genres/" + id + "/edit";

        // method for test
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("id", String.valueOf(genreDto.getId()))
                        .param("name", genreDto.getName())
                        .accept(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is(302))
                .andReturn();
    }

    @DisplayName("экшен удаления Жанра")
    @Test
    void deleteAction() throws Exception {
        long id = 1;
        GenreDto genreDto = new GenreDto(id, "Genre_NEW");
        String url = "/genres/" + id + "/delete";

        // method for test
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("id", String.valueOf(genreDto.getId()))
                        .param("name", genreDto.getName())
                        .accept(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is(302))
                .andReturn();
    }
}