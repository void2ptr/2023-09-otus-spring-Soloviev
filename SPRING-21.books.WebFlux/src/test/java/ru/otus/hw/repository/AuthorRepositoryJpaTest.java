//package ru.otus.hw.repository;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ArgumentsSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import ru.otus.hw.data.AuthorsArgumentsProvider;
//import ru.otus.hw.data.InitTestData;
//import ru.otus.hw.model.Author;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DisplayName("Репозиторий на основе JPA для работы с авторами книг")
//@DataJpaTest
//class AuthorRepositoryJpaTest {
//
//    @Autowired
//    private AuthorRepository authorRepository;
//
//    private List<Author> dbAuthors;
//
//    @BeforeEach
//    void setUp() {
//        dbAuthors = InitTestData.getDbAuthors();
//    }
//
//    @DisplayName("должен загружать список всех авторов")
//    @Test
//    void findAll() {
//        // init
//        var expectedAuthor = dbAuthors;
//        // method for test
//        var actualAuthor = authorRepository.findAll();
//        // check
//        assertThat(actualAuthor).containsExactlyElementsOf(expectedAuthor);
//        actualAuthor.forEach(System.out::println);
//    }
//
//    @DisplayName("должен загружать авторов по id")
//    @ParameterizedTest
//    @ArgumentsSource(AuthorsArgumentsProvider.class)
//    void findById(Author expectedAuthor) {
//        // method for test
//        var actualAuthor = authorRepository.findAuthorById(expectedAuthor.getId());
//        // check
//        assertThat(actualAuthor).isPresent();
//        assertThat(actualAuthor.get()).isEqualTo(expectedAuthor);
//    }
//}
