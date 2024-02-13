package ru.otus.hw.security;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.test.context.support.WithMockUser;
import ru.otus.hw.config.DatabaseConfig;
import ru.otus.hw.data.AclRepository;
import ru.otus.hw.data.BooksArgumentsProvider;
import ru.otus.hw.data.CommentsArgumentsProvider;
import ru.otus.hw.data.InitTestData;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Comment;
import ru.otus.hw.repository.CommentRepository;
import ru.otus.hw.security.acl.AclConfig;
import ru.otus.hw.security.acl.PermissionService;
import ru.otus.hw.service.CommentServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@DisplayName("ACL security для Комментариев")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({AclConfig.class, PermissionService.class, DatabaseConfig.class, AclRepository.class,
        CommentServiceImpl.class })
public class CommentAclSecurityTest {

    private static final String ACL_CLASS = "ru.otus.hw.model.Comment";

    @Autowired
    private AclConfig aclConfig;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private AclRepository aclRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentServiceImpl commentService;


    @DisplayName("ACL не должен загружать список всех Комментариев")
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @ParameterizedTest
    @ArgumentsSource(BooksArgumentsProvider.class)
    void findAll(Book book) {
        // init
        var comments = commentRepository.findCommentsByBookId(book.getId());
        comments.forEach(comment ->
                aclRepository.setGranting(ACL_CLASS, comment.getId(), BasePermission.READ, false));

        // method for test
        var actual = commentRepository.findCommentsByBookId(book.getId());
        assertThat(actual).isEmpty();

        // clean
        comments.forEach(comment ->
                aclRepository.setGranting(ACL_CLASS, comment.getId(), BasePermission.READ, true));
    }

    @DisplayName("ACL не должен загружать Комментарий")
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @ParameterizedTest
    @ArgumentsSource(CommentsArgumentsProvider.class)
    void findById(Comment expectedComment) {
        // init
        aclRepository.setGranting(ACL_CLASS, expectedComment.getId(), BasePermission.READ, false);

        // method for test
        assertThatThrownBy(() -> commentRepository.findCommentById(expectedComment.getId()))
                .isInstanceOf(org.springframework.security.access.AccessDeniedException.class)
                .hasMessageContaining("Access Denied");

        // clean
        aclRepository.setGranting(ACL_CLASS, expectedComment.getId(), BasePermission.READ, true);
    }

    @DisplayName("ACL не должен добавлять Комментарий")
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @Test
    void insert() {
        // init
        Long id = 0L;
        Book book = InitTestData.getDbBooks().get(1);
        aclRepository.setGranting(ACL_CLASS, id, BasePermission.CREATE, false);

        // method for test
        assertThatThrownBy(() -> commentService.insert(new Comment(id, "Insert Comment", book)))
                .isInstanceOf(org.springframework.security.access.AccessDeniedException.class)
                .hasMessageContaining("Access Denied");

        // clean
        aclRepository.setGranting(ACL_CLASS, id, BasePermission.CREATE, true);
    }

    @DisplayName("ACL не должен редактировать Комментарий")
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @ParameterizedTest
    @ArgumentsSource(CommentsArgumentsProvider.class)
    void update(Comment expectedComment) {
        // init
        aclRepository.setGranting(ACL_CLASS, expectedComment.getId(), BasePermission.WRITE, false);

        // method for test
        assertThatThrownBy(() -> commentService.update(expectedComment))
                .isInstanceOf(org.springframework.security.access.AccessDeniedException.class)
                .hasMessageContaining("Access Denied");

        // clean
        aclRepository.setGranting(ACL_CLASS, expectedComment.getId(), BasePermission.WRITE, true);
    }

    @DisplayName("ACL не должен удалять Комментарий")
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @ParameterizedTest
    @ArgumentsSource(CommentsArgumentsProvider.class)
    void delete(Comment expectedComment) {
        // init
        aclRepository.setGranting(ACL_CLASS, expectedComment.getId(), BasePermission.DELETE, false);

        // method for test
        assertThatThrownBy(() -> commentRepository.deleteById(expectedComment.getId()))
                .isInstanceOf(org.springframework.security.access.AccessDeniedException.class)
                .hasMessageContaining("Access Denied");

        // clean
        aclRepository.setGranting(ACL_CLASS, expectedComment.getId(), BasePermission.DELETE, true);
    }

}
