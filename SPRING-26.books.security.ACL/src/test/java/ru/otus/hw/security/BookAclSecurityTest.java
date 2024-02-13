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
import ru.otus.hw.data.BooksArgumentsProvider;
import ru.otus.hw.data.GenresArgumentsProvider;
import ru.otus.hw.data.InitTestData;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.security.acl.AclConfig;
import ru.otus.hw.security.acl.PermissionService;
import ru.otus.hw.service.BookServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@DisplayName("ACL security для Книг")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({AclConfig.class, PermissionService.class, DatabaseConfig.class, AclRepository.class,
        BookServiceImpl.class })
public class BookAclSecurityTest {

    private static final String ACL_CLASS = "ru.otus.hw.model.Book";

    @Autowired
    private AclConfig aclConfig;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private AclRepository aclRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookServiceImpl bookService;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbBooks = InitTestData.getDbBooks();
    }


    @DisplayName("ACL не должен загружать список всех книг")
    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void findAll() {
        // init
        dbBooks.forEach(book -> aclRepository.setGranting(ACL_CLASS, book.getId(), BasePermission.READ, false));

        // method for test
        var actualBook = bookRepository.findAll();
        // check
        assertThat(actualBook).isEmpty();

        // clean
        dbBooks.forEach(book -> aclRepository.setGranting(ACL_CLASS, book.getId(), BasePermission.READ, true));

    }

    @DisplayName("ACL не должен загружать книгу")
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @ParameterizedTest
    @ArgumentsSource(BooksArgumentsProvider.class)
    void findBookById(Book expectedBook) {
        // init
        aclRepository.setGranting(ACL_CLASS, expectedBook.getId(), BasePermission.READ, false);

        // method for test
        assertThatThrownBy(() -> bookRepository.findBookById(expectedBook.getId()))
                .isInstanceOf(org.springframework.security.access.AccessDeniedException.class)
                .hasMessageContaining("Access Denied");

        // clean
        aclRepository.setGranting(ACL_CLASS, expectedBook.getId(), BasePermission.READ, true);
    }

    @DisplayName("ACL не должен загружать список книг по ID автора")
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @ParameterizedTest
    @ArgumentsSource(AuthorsArgumentsProvider.class)
    void findByAuthorId(Author author) {
        // init
        dbBooks.forEach(book -> aclRepository.setGranting(ACL_CLASS, book.getId(), BasePermission.READ, false));

        // method for test
        var actualBook = bookRepository.findByAuthorId(author.getId());
        // check
        assertThat(actualBook).isEmpty();

        // clean
        dbBooks.forEach(book -> aclRepository.setGranting(ACL_CLASS, book.getId(), BasePermission.READ, true));

    }

    @DisplayName("ACL не должен загружать список всех книг по ID жанра")
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @ArgumentsSource(GenresArgumentsProvider.class)
    void findByGenresId(Genre genre) {
        // init
        dbBooks.forEach(book -> aclRepository.setGranting(ACL_CLASS, book.getId(), BasePermission.READ, false));

        // method for test
        var actualBook = bookRepository.findByGenresId(genre.getId());
        // check
        assertThat(actualBook).isEmpty();

        // clean
        dbBooks.forEach(book -> aclRepository.setGranting(ACL_CLASS, book.getId(), BasePermission.READ, true));

    }
    @DisplayName("ACL не должен добавлять книгу")
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @Test
    void insert() {
        // init
        Long id = 0L;
        var book = dbBooks.get(1);
        book.setId(id);
        aclRepository.setGranting(ACL_CLASS, id, BasePermission.CREATE, false);

        // method for test
        assertThatThrownBy(() -> bookService.insert(book))
                .isInstanceOf(org.springframework.security.access.AccessDeniedException.class)
                .hasMessageContaining("Access Denied");

        // clean
        aclRepository.setGranting(ACL_CLASS, id, BasePermission.CREATE, true);
    }

    @DisplayName("ACL не должен редактировать книгу")
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @ParameterizedTest
    @ArgumentsSource(BooksArgumentsProvider.class)
    void update(Book expectedBook) {
        // init
        aclRepository.setGranting(ACL_CLASS, expectedBook.getId(), BasePermission.WRITE, false);

        // method for test
        assertThatThrownBy(() -> bookService.update(expectedBook))
                .isInstanceOf(org.springframework.security.access.AccessDeniedException.class)
                .hasMessageContaining("Access Denied");

        // clean
        aclRepository.setGranting(ACL_CLASS, expectedBook.getId(), BasePermission.WRITE, true);
    }

    @DisplayName("ACL не должен удалять книгу")
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @ParameterizedTest
    @ArgumentsSource(BooksArgumentsProvider.class)
    void delete(Book expectedBook) {
        // init
        aclRepository.setGranting(ACL_CLASS, expectedBook.getId(), BasePermission.DELETE, false);

        // method for test
        assertThatThrownBy(() -> bookRepository.deleteById(expectedBook.getId()))
                .isInstanceOf(org.springframework.security.access.AccessDeniedException.class)
                .hasMessageContaining("Access Denied");

        // clean
        aclRepository.setGranting(ACL_CLASS, expectedBook.getId(), BasePermission.DELETE, true);
    }

}
