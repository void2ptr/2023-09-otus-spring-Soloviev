package ru.otus.hw.repository;

import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.mapper.BookR2Mapper;

public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final DatabaseClient client;

    private final BookR2Mapper mapper;

    public BookRepositoryCustomImpl(DatabaseClient client, BookR2Mapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    @Override
    public Flux<BookDto> findAllBooks() {
        return client.sql("""
            SELECT b.id                                                     as book_id
                 , b.title                                                  as book_title
                 , JSON_OBJECT('id': a.id, 'fullName': a.full_name)         as author
                 , json_arrayAgg(JSON_OBJECT('id': g.id, 'name': g."name")) as genres
            FROM public.book b
            inner join public.author      a on a.id = b.author_id
            inner join public.book_genre bg on bg.book_id = b.id
            inner join public.genre       g on g.id = bg.genre_id
            group by b.id, b.title, a.id, a.full_name
            order by b.id, b.title
            """)
                .map(mapper::apply)
                .all();
    }

}
