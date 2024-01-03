package ru.otus.hw.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.data.CommentsArgumentsProvider;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.mapper.CommentMapper;
import ru.otus.hw.model.Comment;
import ru.otus.hw.service.CommentService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("проверка раздела для Комментариев")
@WebMvcTest(CommentsController.class)
class CommentsControllerTest {
//    private static final String BASE_URL = "/api/v1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper mapper;

    @DisplayName("проверка получения списка Комментариев")
    @ParameterizedTest()
    @ArgumentsSource(CommentsArgumentsProvider.class)
    void shouldReturnCommentsForBook(Comment comment) throws Exception {
        CommentDto commentsDto = CommentMapper.toDto(comment);
        String url = "/api/v1/books/" + commentsDto.getBook().getId() + "/comments";

        List<CommentDto> expected = new ArrayList<>();
        expected.add(new CommentDto(1, "Comment_1", commentsDto.getBook()));
        expected.add(new CommentDto(2, "Comment_2", commentsDto.getBook()));
        given(commentService.findCommentByBookId(commentsDto.getBook().getId())).willReturn(expected);

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }

    @DisplayName("экшен добавления Комментария")
    @ParameterizedTest()
    @ArgumentsSource(CommentsArgumentsProvider.class)
    void insetAction(Comment comment) throws Exception {
        // init
        CommentDto commentDto = CommentMapper.toDto(comment);
        String url = "/api/v1/books/" + commentDto.getBook().getId() + "/comments";
        given(commentService.insert(any())).willReturn(commentDto);
        String expected = mapper.writeValueAsString(commentDto);

        // method for test
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", String.valueOf(commentDto.getBook().getId()))
                        .content(expected)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @DisplayName("экшен редактирования Комментария")
    @ParameterizedTest()
    @ArgumentsSource(CommentsArgumentsProvider.class)
    void updateAction(Comment comment) throws Exception {
        String url = "/api/v1/books/" + comment.getBook().getId() + "/comments/" + comment.getId();
        CommentDto commentDto = CommentMapper.toDto(comment);
        given(commentService.update(any())).willReturn(commentDto);
        String expected = mapper.writeValueAsString(commentDto);

        // method for test
        mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("bookId", String.valueOf(comment.getBook().getId()))
                        .param("commentId", String.valueOf(comment.getId()))
                        .content(expected)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @DisplayName("экшен удаления Комментария")
    @ParameterizedTest()
    @ArgumentsSource(CommentsArgumentsProvider.class)
    void deleteAction(Comment comment) throws Exception {
        String url = "/api/v1/books/" + comment.getBook().getId() + "/comments/" + comment.getId();
        CommentDto commentDto = CommentMapper.toDto(comment);
        given(commentService.delete(comment.getId())).willReturn(commentDto);
        String expected = mapper.writeValueAsString(commentDto);

        // method for test
        mockMvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("bookId", String.valueOf(comment.getBook().getId()))
                        .param("commentId", String.valueOf(comment.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

}