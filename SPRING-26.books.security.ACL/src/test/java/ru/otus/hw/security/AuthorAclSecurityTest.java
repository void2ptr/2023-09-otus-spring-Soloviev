package ru.otus.hw.security;


import org.junit.jupiter.api.BeforeEach;
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
import ru.otus.hw.data.AuthorsArgumentsProvider;
import ru.otus.hw.data.InitTestData;
import ru.otus.hw.model.Author;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.security.acl.AclConfig;
import ru.otus.hw.security.acl.PermissionService;
import ru.otus.hw.service.AuthorServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@DisplayName("ACL security для Автора")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({AclConfig.class, PermissionService.class, DatabaseConfig.class, AclRepository.class,
        AuthorServiceImpl.class })
public class AuthorAclSecurityTest {

    private static final String ACL_CLASS = "ru.otus.hw.model.Author";

    @Autowired
    private AclConfig aclConfig;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private AclRepository aclRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorServiceImpl authorService;

    private List<Author> dbAuthors;

    @BeforeEach
    void setUp() {
        dbAuthors = InitTestData.getDbAuthors();
    }


    @DisplayName("ACL не должен загружать список всех авторов")
    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void findAll() {
        // init
        dbAuthors.forEach(author ->
                aclRepository.setGranting(ACL_CLASS, author.getId(), BasePermission.READ, false));

        // method for test
        var actualAuthor = authorRepository.findAll();
        // check
        assertThat(actualAuthor).isEmpty();

        // clean
        dbAuthors.forEach(author ->
                aclRepository.setGranting(ACL_CLASS, author.getId(), BasePermission.READ, true));

    }

    @DisplayName("ACL не должен загружать автора")
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @ParameterizedTest
    @ArgumentsSource(AuthorsArgumentsProvider.class)
    void findById(Author expectedAuthor) {
        // init
        aclRepository.setGranting(ACL_CLASS, expectedAuthor.getId(), BasePermission.READ, false);

        // method for test
        assertThatThrownBy(() -> authorRepository.findAuthorById(expectedAuthor.getId()))
                .isInstanceOf(org.springframework.security.access.AccessDeniedException.class)
                .hasMessageContaining("Access Denied");

        // clean
        aclRepository.setGranting(ACL_CLASS, expectedAuthor.getId(), BasePermission.READ, true);
    }

    @DisplayName("ACL не должен добавлять автора")
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @Test
    void insert() {
        // init
        Long id = 0L;
        aclRepository.setGranting(ACL_CLASS, id, BasePermission.CREATE, false);

        // method for test
        assertThatThrownBy(() -> authorService.insert(new Author(id, "Insert Author")))
                .isInstanceOf(org.springframework.security.access.AccessDeniedException.class)
                .hasMessageContaining("Access Denied");

        // clean
        aclRepository.setGranting(ACL_CLASS, id, BasePermission.CREATE, true);
    }

    @DisplayName("ACL не должен редактировать автора")
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @ParameterizedTest
    @ArgumentsSource(AuthorsArgumentsProvider.class)
    void update(Author expectedAuthor) {
        // init
        aclRepository.setGranting(ACL_CLASS, expectedAuthor.getId(), BasePermission.WRITE, false);

        // method for test
        assertThatThrownBy(() -> authorService.update(expectedAuthor))
                .isInstanceOf(org.springframework.security.access.AccessDeniedException.class)
                .hasMessageContaining("Access Denied");

        // clean
        aclRepository.setGranting(ACL_CLASS, expectedAuthor.getId(), BasePermission.WRITE, true);
    }

    @DisplayName("ACL не должен удалять автора")
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @ParameterizedTest
    @ArgumentsSource(AuthorsArgumentsProvider.class)
    void delete(Author expectedAuthor) {
        // init
        aclRepository.setGranting(ACL_CLASS, expectedAuthor.getId(), BasePermission.DELETE, false);

        // method for test
        assertThatThrownBy(() -> authorRepository.delete(expectedAuthor))
                .isInstanceOf(org.springframework.security.access.AccessDeniedException.class)
                .hasMessageContaining("Access Denied");

        // clean
        aclRepository.setGranting(ACL_CLASS, expectedAuthor.getId(), BasePermission.DELETE, true);
    }

}
