package ru.otus.hw.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.data.AuthorsArgumentsProvider;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.model.Author;
import ru.otus.hw.service.AuthorService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("проверка раздела для Авторов")
@WebMvcTest(AuthorsController.class)
public class AuthorsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    public void init(){
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    @DisplayName("проверка получения списка Авторов")
    @Test
    void findAll() throws Exception {
        // test
        List<AuthorDto> expected = List.of(
                new AuthorDto(1, "Author_1"),
                new AuthorDto(2, "Author_2"));
        given(authorService.findAll()).willReturn(expected);

        // test
        mockMvc.perform(get("/api/v1/authors"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }


    @DisplayName("проверка получения списка Авторов")
    @ParameterizedTest()
    @ArgumentsSource(AuthorsArgumentsProvider.class)
    void findById(Author author) throws Exception {
        AuthorDto expected = AuthorMapper.toDto(author);
        // test
        given(authorService.findAuthorById(author.getId())).willReturn(expected);


        // test
        mockMvc.perform(get("/api/v1/authors/"+ author.getId())
                .param("id", String.valueOf(author.getId())))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }

    @DisplayName("экшен добавления Автора")
    @ParameterizedTest()
    @ArgumentsSource(AuthorsArgumentsProvider.class)
    void insertAction(Author author) throws Exception {
        AuthorDto actual = AuthorMapper.toDto(author);
        // init
        given(authorService.insert(any())).willReturn(actual);
        String expected = mapper.writeValueAsString(AuthorMapper.toDto(author));

        // test
        mockMvc.perform(post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expected)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @DisplayName("экшен редактирование Автора")
    @ParameterizedTest()
    @ArgumentsSource(AuthorsArgumentsProvider.class)
    void updateAction(Author author) throws Exception {
        AuthorDto actual = AuthorMapper.toDto(author);
        // init
        given(authorService.update(any())).willReturn(actual);
        String expected = mapper.writeValueAsString(AuthorMapper.toDto(author));

        // test
        mockMvc.perform(put("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expected)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @DisplayName("экшен удаление Автора")
    @ParameterizedTest()
    @ArgumentsSource(AuthorsArgumentsProvider.class)
    void deleteAction(Author author) throws Exception {
        AuthorDto actual = AuthorMapper.toDto(author);
        // init
        given(authorService.delete(author.getId())).willReturn(actual);
        String expected = mapper.writeValueAsString(AuthorMapper.toDto(author));

        // test
        mockMvc.perform(delete("/api/v1/authors/" + author.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", String.valueOf(author.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }
}