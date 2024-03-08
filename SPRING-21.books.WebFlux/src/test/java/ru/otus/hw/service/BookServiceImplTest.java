package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.hw.dto.BookDto;


@SpringBootTest
class BookServiceImplTest {
    @Autowired
    private BookServiceImpl bookServiceImpl;

    @Test
    void findAll() {
        // test
        Flux<BookDto> bookDtoFlux = bookServiceImpl.findAll();
//        bookDtoFlux.log().subscribe(System.out::println);
        StepVerifier
                .create(bookDtoFlux)
                .expectNextMatches(BookDto -> BookDto.getClass() == BookDto.class)
                .thenConsumeWhile(x -> true)
                .verifyComplete();
    }

    @Test
    void findById() {
        Long bookId = 1L;
        // test
        Mono<BookDto> bookDtoMono = bookServiceImpl.findById(bookId);
//        bookDtoMono.log().subscribe(System.out::println);
        StepVerifier
                .create(bookDtoMono)
                .expectNextMatches(BookDto -> BookDto.getId().equals(bookId))
                .thenConsumeWhile(x -> true)
                .verifyComplete();
    }

}
