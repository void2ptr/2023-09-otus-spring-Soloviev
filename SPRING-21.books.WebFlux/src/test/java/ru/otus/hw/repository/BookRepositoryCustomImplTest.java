package ru.otus.hw.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;


@DisplayName("Репозиторий на основе R2DBC для работы с Books and Genres Custom")
@SpringBootTest
class BookRepositoryCustomImplTest {

    @Autowired
    private BookRepositoryCustomImpl repository;

    @Test
    void findAllBooks() {
        // test
        var bookGenreFlux = repository.findAllBooks();
        bookGenreFlux.log().subscribe(System.out::println);
        StepVerifier
                .create(bookGenreFlux)
                .expectNextMatches(bg -> bg.getId() != null )
                .thenConsumeWhile(x -> true)
                .verifyComplete();
    }
}