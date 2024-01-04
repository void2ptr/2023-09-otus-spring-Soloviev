package ru.otus.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.service.AuthorService;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("проверка раздела для Авторов")
@WebMvcTest(AuthorsController.class)
public class AuthorsControllerTest {
    private static final String BASE_URL = "/api/v1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @Autowired
    private ObjectMapper mapper;


    @DisplayName("проверка открытия страницы с списком Авторов")
    @Test
    void listPage() throws Exception {
        // test
        String url = BASE_URL + "/authors";
        List<AuthorDto> authorDtoList = List.of(
                new AuthorDto(1, "Author_1"),
                new AuthorDto(2, "Author_2"));
        given(authorService.findAll()).willReturn(authorDtoList);

        // test
        var content = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(content)
                .contains("Author_1")
                .contains("Author_2");
    }

    @DisplayName("открытие страницы добавления Автора")
    @Test
    void addPage() throws Exception {
        // test
        long id = 0;
        String url = BASE_URL + "/authors/add";
        String expect = "New Author";

        // test
        String content = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(content).contains(expect);
    }

    @DisplayName("открытие страницы редактирования Автора")
    @Test
    void editPage() throws Exception {
        // test
        long id = 1;
        String expect = "Author_EDIT";
        String url = BASE_URL + "/authors/" + id + "/edit";
        AuthorDto authorDto = new AuthorDto(id, expect);
        given(authorService.findAuthorById(id)).willReturn(authorDto);

        // test
        String content = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(content).contains(expect);
    }

    @DisplayName("открытие страницы удаления Автора")
    @Test
    void deletePage() throws Exception {
        // test
        long id = 1;
        String expect = "Author_DELETE";
        String url = BASE_URL + "/authors/" + id + "/delete";
        AuthorDto author = new AuthorDto(id, expect);
        given(authorService.findAuthorById(id)).willReturn(author);

        // test
        String content = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(content).contains(expect);
    }

    @DisplayName("экшен добавления Автора")
    @Test
    void addAction() throws Exception {
        // init
        String url = BASE_URL + "/authors/add";
        AuthorDto authorDto = new AuthorDto(0, "Author_NEW");
        given(authorService.insert(authorDto)).willReturn(Optional.of(authorDto));

        // test
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("id", String.valueOf(authorDto.getId()))
                        .param("fullName", authorDto.getFullName())
                        .accept(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is(302))
                .andReturn();
    }


    @DisplayName("экшен редактирование Автора")
    @Test
    void updateAction() throws Exception {
        // init
        String url = BASE_URL + "/authors/1/edit";
        AuthorDto authorDto = new AuthorDto(0, "Author_NEW");
        given(authorService.update(authorDto)).willReturn(Optional.of(authorDto));

        // test
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("id", String.valueOf(authorDto.getId()))
                        .content(mapper.writeValueAsBytes(authorDto))
                        .accept(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is(302))
                .andReturn();
    }

    @DisplayName("экшен удаление Автора")
    @Test
    void deleteAction() throws Exception {
        // init
        long authorId = 1;
        String url = BASE_URL + "/authors/" + authorId + "/delete";
        given(authorService.delete(authorId)).willReturn(true);

        // test
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("id", String.valueOf(authorId))
                        .accept(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is(302))
                .andReturn();
    }
}