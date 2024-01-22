package ru.otus.hw.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controller.AuthorsController;
import ru.otus.hw.data.UrlAndUser;
import ru.otus.hw.data.UrlAuthorArgumentsProvider;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.model.Role;
import ru.otus.hw.service.AuthorService;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("проверка раздела для Авторов на безопасность")
@WebMvcTest(AuthorsController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorsControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @DisplayName("должен открыть страницу с проверкой прав")
    @ParameterizedTest
    @ArgumentsSource(UrlAuthorArgumentsProvider.class)
    void openPageOk(UrlAndUser urlAndUser) throws Exception  {

        AuthorDto authorDto = new AuthorDto(1L, "Author_1");
        given(authorService.findAuthorById(4L)).willReturn(authorDto);

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

    @DisplayName("не должен открыть страницу со списком Авторов")
    @ParameterizedTest
    @ArgumentsSource(UrlAuthorArgumentsProvider.class)
    void openPageAnonymous(UrlAndUser urlAndUser) throws Exception  {
        // test
        mockMvc.perform(get(urlAndUser.url())
                        .with(csrf())
                        .with(anonymous()))
                .andExpect(status().is4xxClientError());
    }

}