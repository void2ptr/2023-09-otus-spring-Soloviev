package ru.otus.hw.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controller.CommentsController;
import ru.otus.hw.data.UrlAndUser;
import ru.otus.hw.data.UrlCommentsArgumentsProvider;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.model.Role;
import ru.otus.hw.service.CommentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("проверка раздела для Комментариев на безопасность")
@WebMvcTest(CommentsController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentsControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @DisplayName("должен открыть страницу с проверкой прав")
    @ParameterizedTest
    @ArgumentsSource(UrlCommentsArgumentsProvider.class)
    void openPageOk(UrlAndUser urlAndUser) throws Exception  {
        BookDto bookDto = new BookDto(1L, "Title", new AuthorDto(0, "Author"),
                List.of(new GenreDto(0, "Genre")));

        List<CommentDto> comments = new ArrayList<>();
        comments.add(new CommentDto(1, "Comment_1", bookDto));
        comments.add(new CommentDto(2, "Comment_2", bookDto));
        given(commentService.findCommentByBookId(1L)).willReturn(comments);
        given(commentService.findBookById(1L)).willReturn(Optional.of(bookDto));
        CommentDto commentDto = new CommentDto(4L, "new comment", bookDto);
        given(commentService.findCommentById(4L)).willReturn(Optional.of(commentDto));

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
    @ArgumentsSource(UrlCommentsArgumentsProvider.class)
    void openPageAnonymous(UrlAndUser urlAndUser) throws Exception  {
        // test
        mockMvc.perform(get(urlAndUser.url())
                        .with(csrf())
                        .with(anonymous()))
                .andExpect(status().is4xxClientError());
    }

}