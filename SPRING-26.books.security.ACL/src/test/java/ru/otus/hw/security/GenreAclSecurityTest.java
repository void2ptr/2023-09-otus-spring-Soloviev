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
import ru.otus.hw.data.GenresArgumentsProvider;
import ru.otus.hw.data.InitTestData;
import ru.otus.hw.model.Genre;
import ru.otus.hw.repository.GenreRepository;
import ru.otus.hw.security.acl.AclConfig;
import ru.otus.hw.security.acl.PermissionService;
import ru.otus.hw.service.GenreServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@DisplayName("ACL security для Жанра")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({AclConfig.class, PermissionService.class, DatabaseConfig.class, AclRepository.class,
        GenreServiceImpl.class })
public class GenreAclSecurityTest {

    private static final String ACL_CLASS = "ru.otus.hw.model.Genre";

    @Autowired
    private AclConfig aclConfig;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private AclRepository aclRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private GenreServiceImpl genreService;

    private List<Genre> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = InitTestData.getDbGenres();
    }

    @DisplayName("ACL НЕ должен загружать список всех Жанров")
    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void findAll() {
        // init
        dbGenres.forEach(genre -> aclRepository.setGranting(ACL_CLASS, genre.getId(), BasePermission.READ, false));

        // method for test
        var actual = genreRepository.findAll();
        // check
        assertThat(actual).isEmpty();

        // clean
        dbGenres.forEach(genre -> aclRepository.setGranting(ACL_CLASS, genre.getId(), BasePermission.READ, true));

    }

    @DisplayName("ACL НЕ должен загружать жанр")
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @ParameterizedTest
    @ArgumentsSource(GenresArgumentsProvider.class)
    void findById(Genre expectedGenre) {
        // init
        aclRepository.setGranting(ACL_CLASS, expectedGenre.getId(), BasePermission.READ,false);

        // method for test
        assertThatThrownBy(() -> genreRepository.findGenreById(expectedGenre.getId()))
                .isInstanceOf(org.springframework.security.access.AccessDeniedException.class)
                .hasMessageContaining("Access Denied");

        // clean
        aclRepository.setGranting(ACL_CLASS, expectedGenre.getId(), BasePermission.READ,true);
    }

    @DisplayName("ACL НЕ должен загружать список Жанров")
    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void findGenresByIdIn() {
        // init
        List<Long> genresId = List.of(1L, 2L, 3L);
        genresId.forEach(id -> aclRepository.setGranting(ACL_CLASS, id, BasePermission.READ, false));

        // method for test
        var actual = genreRepository.findGenresByIdIn(genresId);
        // check
        assertThat(actual).isEmpty();

        // clean
        genresId.forEach(id -> aclRepository.setGranting(ACL_CLASS, id, BasePermission.READ, true));

    }

    @DisplayName("ACL НЕ должен добавлять жанр")
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @Test
    void insert() {
        // init
        Long id = 0L;
        aclRepository.setGranting(ACL_CLASS, id, BasePermission.CREATE, false);

        // method for test
        assertThatThrownBy(() -> genreService.insert(new Genre(id, "Insert Genre")))
                .isInstanceOf(org.springframework.security.access.AccessDeniedException.class)
                .hasMessageContaining("Access Denied");

        // clean
        aclRepository.setGranting(ACL_CLASS, id, BasePermission.CREATE, true);
    }

    @DisplayName("ACL НЕ должен редактировать жанр")
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @ParameterizedTest
    @ArgumentsSource(GenresArgumentsProvider.class)
    void update(Genre expectedGenre) {
        // init
        aclRepository.setGranting(ACL_CLASS, expectedGenre.getId(), BasePermission.WRITE, false);

        // method for test
        assertThatThrownBy(() -> genreService.update(expectedGenre))
                .isInstanceOf(org.springframework.security.access.AccessDeniedException.class)
                .hasMessageContaining("Access Denied");

        // clean
        aclRepository.setGranting(ACL_CLASS, expectedGenre.getId(), BasePermission.WRITE, true);
    }

    @DisplayName("ACL НЕ должен удалять жанр")
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @ParameterizedTest
    @ArgumentsSource(GenresArgumentsProvider.class)
    void delete(Genre expectedGenre) {
        // init
        aclRepository.setGranting(ACL_CLASS, expectedGenre.getId(), BasePermission.DELETE, false);

        // method for test
        assertThatThrownBy(() -> genreRepository.delete(expectedGenre))
                .isInstanceOf(org.springframework.security.access.AccessDeniedException.class)
                .hasMessageContaining("Access Denied");

        // clean
        aclRepository.setGranting(ACL_CLASS, expectedGenre.getId(), BasePermission.DELETE, true);
    }
}
