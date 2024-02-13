package ru.otus.hw.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.data.InitTestData;
import ru.otus.hw.model.User;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.GenreRepository;
import ru.otus.hw.service.AuthorService;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.CommentService;
import ru.otus.hw.service.GenreService;

import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;

@DisplayName("Проверка контроллеров на безопасность")
@WebMvcTest
@MockBean(classes = {BookService.class, BookRepository.class, AuthorService.class, AuthorRepository.class,
        GenreService.class, GenreRepository.class, CommentService.class})
@Import({SecurityConfig.class})
public class ControllersSecurityTest {
    @Autowired
    private MockMvc mockMvc;

    @DisplayName("Заданные конечные точки должны быть доступны под заданным юзером")
    @ParameterizedTest
    @ArgumentsSource(SecurityTestArgumentsProvider.class)
    void givenEndpointShouldBeAccessibleUnderTheSpecifiedUser(String method, String url, User user) throws Exception  {
        var requestBuilder = request(HttpMethod.valueOf(method), url).with(user(user));
        mockMvc.perform(requestBuilder).andExpect(authenticated());
    }

    @DisplayName("Заданные конечные точки не должны быть доступны без юзера")
    @ParameterizedTest
    @ArgumentsSource(SecurityTestArgumentsProvider.class)
    void givenEndpointShouldBeUnAccessibleWithoutUser(String method, String url) throws Exception  {
        var requestBuilder = request(HttpMethod.valueOf(method), url);
        mockMvc.perform(requestBuilder).andExpect(unauthenticated());
    }

    private static class SecurityTestArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            List<User> dbUsers = InitTestData.getDbUsers();
            var getUrls = List.of(
                    "/authors", "/authors/add", "/authors/4/edit", "/authors/4/delete",
                    "/books", "/books/add", "/books/4/edit", "/books/4/delete",
                    "/books/1/comments", "/books/1/comments/add", "/books/1/comments/4/edit",
                    "/books/1/comments/4/delete",
                    "/genres", "/genres/add", "/genres/4/edit", "/genres/4/delete");
            var postUrls = List.of(
                    "/authors/add", "/authors/4/edit", "/authors/4/delete",
                    "/books/add", "/books/4/edit", "/books/4/delete", "/books/1/comments/add",
                    "/books/1/comments/4/edit", "/books/1/comments/4/delete",
                    "/genres/add", "/genres/4/edit", "/genres/4/delete");

            var getArgsStream = getUrls.stream().flatMap(url ->
                    dbUsers.stream().map(user -> Arguments.of("get", url, user)));
            var postArgsStream = postUrls.stream().flatMap(url ->
                    dbUsers.stream().map(user -> Arguments.of("post", url, user)));
            return Stream.concat(getArgsStream, postArgsStream);
        }
    }
}
