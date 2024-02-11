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
        String url = "/api/v1/authors";
        List<Author> authors = List.of(
                new Author(1, "Author_1"),
                new Author(2, "Author_2"));
        given(authorService.findAll()).willReturn(authors);
        List<AuthorDto> expected = authors.stream()
                .map(AuthorMapper::toDto)
                .toList();

        // test
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }


    @DisplayName("проверка получения списка Авторов")
    @ParameterizedTest()
    @ArgumentsSource(AuthorsArgumentsProvider.class)
    void findById(Author author) throws Exception {
        // test
        String url = "/api/v1/authors/"+ author.getId();
        given(authorService.findAuthorById(author.getId())).willReturn(author);
        AuthorDto expected = AuthorMapper.toDto(author);

        // test
        mockMvc.perform(get(url)
                .param("id", String.valueOf(author.getId())))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }

    @DisplayName("экшен добавления Автора")
    @ParameterizedTest()
    @ArgumentsSource(AuthorsArgumentsProvider.class)
    void addAction(Author author) throws Exception {
        // init
        String url = "/api/v1/authors";
        given(authorService.insert(author)).willReturn(author);
        String expected = mapper.writeValueAsString(AuthorMapper.toDto(author));

        // test
        mockMvc.perform(post(url)
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
        // init
        String url = "/api/v1/authors/" + author.getId();
        given(authorService.update(author)).willReturn(author);
        String expected = mapper.writeValueAsString(AuthorMapper.toDto(author));

        // test
        mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", String.valueOf(author.getId()))
                        .content(expected)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @DisplayName("экшен удаление Автора")
    @ParameterizedTest()
    @ArgumentsSource(AuthorsArgumentsProvider.class)
    void deleteAction(Author author) throws Exception {
        // init
        String url = "/api/v1/authors/" + author.getId();
        given(authorService.delete(author.getId())).willReturn(author);
        String expected = mapper.writeValueAsString(AuthorMapper.toDto(author));

        // test
        mockMvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", String.valueOf(author.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }
}