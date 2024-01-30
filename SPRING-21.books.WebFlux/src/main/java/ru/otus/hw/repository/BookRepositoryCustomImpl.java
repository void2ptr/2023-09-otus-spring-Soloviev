package ru.otus.hw.repository;

import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.mapper.BookR2ExistMapper;
import ru.otus.hw.mapper.BookR2RowMapper;

public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final DatabaseClient client;

    private final BookR2RowMapper bookR2RowMapper;

    private final BookR2ExistMapper bookR2ExistMapper;

    public BookRepositoryCustomImpl(DatabaseClient client,
                                    BookR2RowMapper bookR2RowMapper,
                                    BookR2ExistMapper bookR2ExistMapper) {
        this.client = client;
        this.bookR2RowMapper = bookR2RowMapper;
        this.bookR2ExistMapper = bookR2ExistMapper;
    }

    @Override
    public Flux<BookDto> findAllBooks() {
        return client.sql("""
                        SELECT b.id                                                     AS book_id
                             , b.title                                                  AS book_title
                             , JSON_OBJECT('id': a.id, 'fullName': a.full_name)         AS author
                             , json_arrayAgg(JSON_OBJECT('id': g.id, 'name': g."name")) AS genres
                        FROM public.book              b
                        INNER JOIN public.author      a ON a.id = b.author_id
                        INNER JOIN public.book_genre bg ON bg.book_id = b.id
                        INNER JOIN public.genre       g ON g.id = bg.genre_id
                        GROUP BY b.id, b.title, a.id, a.full_name
                        ORDER BY b.id, b.title
                        """)
                .map(bookR2RowMapper::apply)
                .all();
    }

    @Override
    public Mono<BookDto> findByBookId(Long bookId) {
        return client.sql("""
                        SELECT b.id                                                     AS book_id
                             , b.title                                                  AS book_title
                             , JSON_OBJECT('id': a.id, 'fullName': a.full_name)         AS author
                             , json_arrayAgg(JSON_OBJECT('id': g.id, 'name': g."name")) AS genres
                        FROM public.book              b
                        INNER JOIN public.author      a ON a.id = b.author_id
                        INNER JOIN public.book_genre bg ON bg.book_id = b.id
                        INNER JOIN public.genre       g ON g.id = bg.genre_id
                        WHERE b.id = :book_id
                        GROUP BY b.id, b.title, a.id, a.full_name
                        """)
                .bind("book_id", bookId)
                .map(bookR2RowMapper::apply)
                .one();
    }

    @Override
    public Mono<Boolean> existByAuthorId(Long authorId) {
        return client.sql("SELECT exists (SELECT b.id FROM book b WHERE b.author_id = :author_id) limit 1")
                .bind("author_id", authorId)
                .map(bookR2ExistMapper::apply)
                .one();
    }
}
