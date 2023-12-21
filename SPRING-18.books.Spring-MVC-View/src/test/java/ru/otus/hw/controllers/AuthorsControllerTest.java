package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.otus.hw.Application;
import ru.otus.hw.dto.AuthorDto;;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.services.AuthorService;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {AuthorsController.class})
//@WebAppConfiguration

@DisplayName("проверка web для Авторов")
//@RunWith(SpringRunner.class)
@WebMvcTest(AuthorsController.class)
public class AuthorsControllerTest {
    private static final String BASE_URL = "/api/v1";

//    @Autowired
//    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private AuthorRepository authorRepository;

    @Autowired
    private ObjectMapper mapper;

//    @BeforeEach
//    public void SetupContext() {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
//    }

//    @Test
//    public void givenWacWhenServletContextThenItController() {
//        ServletContext servletContext = webApplicationContext.getServletContext();
//
//        assertThat(servletContext).isNotNull();
////        assertThat(servletContext).isInstanceOf(MockServletContext);
//        assertThat(webApplicationContext.getBean("authorsController")).isNotNull();
//    }

    @DisplayName("проверка открытия страницы с списком Авторов")
    @Test
    void listPage()  throws Exception {
        String url = BASE_URL + "/author";
        List<AuthorDto> authors = List.of(
                new AuthorDto(1, "Author_1"),
                new AuthorDto(2, "Author_2"));
        given(authorService.findAll()).willReturn(authors);

        var result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(result)
                .contains("Author_1")
                .contains("Author_2");
    }

    @DisplayName("открытие страницы добавления Автора")
    @Test
    void addPage() throws Exception {
        String url = BASE_URL + "/author/0/add";

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("New Author");
    }

    @DisplayName("открытие страницы редактирования Автора")
    @Test
    void editPage() throws Exception {
        String url = BASE_URL + "/author/1/edit";
        String expect = "Author_EDIT";
        AuthorDto author = new AuthorDto(1, expect);
        given(authorService.findAuthorById(1)).willReturn(author);

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains(expect);
    }

    @DisplayName("открытие страницы удаления Автора")
    @Test
    void deletePage() throws Exception {
        String url = BASE_URL + "/author/1/delete";
        String expect = "Author_DELETE";
        AuthorDto author = new AuthorDto(1, expect);
        given(authorService.findAuthorById(1)).willReturn(author);

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains(expect);
    }

    @DisplayName("экшен добавления Автора")
    @Test
    void addAction() throws Exception {

        // init
        String url = BASE_URL + "/author/0/add";
        AuthorDto authorDto = new AuthorDto(0, "Author_NEW");

        given(authorService.insert(authorDto)).willReturn(Optional.of(authorDto));

        // FIXME mock работает
        System.out.println("1 mock OK  ++ " +  authorService);
        System.out.println("1 mock OK  >> " +  authorService.insert(authorDto)); // тут все ок
        System.out.println("1 mock OK  >> " +  authorService.insert(authorDto)); // тут все ок
        System.out.println("1 mock OK  >> " +  authorService.insert(authorDto)); // тут все ок

        // method for test - err
        var mvcResult = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("id", String.valueOf(authorDto.getId()))
                        .param("fullName", authorDto.getFullName())
                        .accept(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.forwardedUrlPattern(BASE_URL + "/author"));


        // method for test - err
//        var mvcResult = mockMvc.perform(post(url)
//                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .content("id=0&fullName=Author_NEW") // OK + APPLICATION_FORM_URLENCODED_VALUE
//                        .accept(MediaType.APPLICATION_FORM_URLENCODED))
//                .andExpect(status().isMovedTemporarily())
//                .andExpect(MockMvcResultMatchers.forwardedUrlPattern(BASE_URL + "/author"));

        // method for test - err - org.springframework.web.bind.MissingServletRequestParameterException
//        var mvcResult = mockMvc.perform(post(url)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .content("""
//                                 { "id": "0","fullName": "Author_NEW" }
//                                 """)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isMovedTemporarily())
//                .andExpect(MockMvcResultMatchers.forwardedUrlPattern(BASE_URL + "/author"));

//        var mvcResult = mockMvc.perform(post(url)
//
////                       .content(MediaType.APPLICATION_JSON_VALUE)
////                       .accept(MediaType.APPLICATION_JSON)
//                       .contentType(MediaType.APPLICATION_FORM_URLENCODED)
////                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
////                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
//
//                        .accept(MediaType.APPLICATION_FORM_URLENCODED)
//                        .characterEncoding(StandardCharsets.UTF_8)
//
////                        .content(mapper.writeValueAsBytes(authorDto))) // ERR - JSON - APPLICATION_JSON_VALUE

//
//                .andExpect(status().isMovedTemporarily())
//                .andExpect(MockMvcResultMatchers.forwardedUrlPattern(BASE_URL + "/author"));
////                .andReturn()
////                .getResponse()
////                .getContentAsString();

//        System.out.println(mvcResult);
//                .andExpect(jsonPath("$.message").value("Username already taken"))
//                .andExpect(status().isMovedTemporarily())
//                .andReturn();
//        Exception resolvedException = mvcResult.getResolvedException();
//        MockHttpServletResponse response = mvcResult.getResponse();
//        MockHttpServletRequest request = mvcResult.getRequest();

    }


    @DisplayName("экшен редактирование Автора")
    @Test
    void updateAction()  throws Exception  {
        String url = BASE_URL + "/author/1/update";

    }

    @DisplayName("экшен удаление Автора")
    @Test
    void deleteAction() throws Exception  {
        String url = BASE_URL + "/author/1/delete";

    }

    @Test
    void handleNotFound() {
    }
}