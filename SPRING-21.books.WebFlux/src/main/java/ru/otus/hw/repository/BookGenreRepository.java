package ru.otus.hw.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.model.BookGenre;

public interface BookGenreRepository extends ReactiveCrudRepository<BookGenre, Long> {

    @Query("DELETE FROM book_genre WHERE book_id = :book_id")
    Mono<BookGenre> deleteByBookID(@Param("book_id") Long bookId);

    @Query("SELECT bg.id, bg.book_id, bg.genre_id FROM book_genre bg WHERE bg.book_id IN (:ids)")
    Flux<BookGenre> findByBookIdIn(@Param("ids") Long ids);

    @Query("SELECT bg.id, bg.book_id, bg.genre_id FROM book_genre bg WHERE bg.genre_id = :genre_id")
    Flux<BookGenre> findByGenreId(@Param("genre_id") Long genreId);
}

