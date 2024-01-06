package ru.otus.hw.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.model.BookModel;

public interface BookRepository extends ReactiveCrudRepository<BookModel, Long>, BookRepositoryCustom {

//    @NonNull
//    @EntityGraph("book-author-entity-graph")
//    Flux<Book> findAll();

    @EntityGraph("book-author-genres-entity-graph")
    Mono<BookModel> findBookById(long bookId);

    @EntityGraph("book-author-entity-graph")
    Flux<BookModel> findBooksByAuthorId(long authorId);

    @EntityGraph(value = "book-author-genres-entity-graph")
    Flux<BookModel> findBooksByGenresId(long genresId);

}
