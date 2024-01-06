package ru.otus.hw.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.BookModel;
import ru.otus.hw.model.Genre;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@AllArgsConstructor
class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private DatabaseClient client;

    private R2dbcEntityTemplate r2bdc;

    @Override
    public Flux<BookModel> findAll() {

        String query = "SELECT b.id, b.title, b.author_id FROM book b";
        var result = client.sql(query).fetch().all();


        // FIXME get by query
        Map<Long, Book> books = r2bdc.select(Book.class).all()
                .collectMap(Book::id, book -> book).block();
        Map<Long, Author> authors = r2bdc.select(Author.class).all()
                .collectMap(Author::getId, author -> author).block();
        Map<Long, Genre> genres = r2bdc.select(Genre.class).all()
                .collectMap(Genre::getId, genre -> genre).block();
        List<BookGenre> relations = r2bdc.select(BookGenre.class).all()
                .collectList().block();
        return mergeBooksInfo(books, authors, genres, relations);
    }

    private Flux<BookModel> mergeBooksInfo(Map<Long, Book> booksMap,
                                           Map<Long, Author> authorsMap,
                                           Map<Long, Genre> genresMap,
                                           List<BookGenre> relations
                                      ) {
        Map<Long, BookModel> booksModelMap = new HashMap<>();
        relations.forEach(relation -> {
            Long bookId = relation.bookId;
            BookModel bookModel = booksModelMap.get(bookId);
            Genre genre = genresMap.get(relation.genreId());
            if (bookModel == null) {
                Book bookRecord = booksMap.get(relation.bookId);
                Author author = authorsMap.get(bookRecord.authorId());
                booksModelMap.put(bookId, new BookModel(bookRecord.id(), bookRecord.title(), author,
                        new ArrayList<>(List.of(genre))));
            } else {
                bookModel.getGenres().add(genre);
            }
        });
        List<BookModel> list = new ArrayList<>();
        booksModelMap.forEach((k, v) -> list.add(v));
        return Flux.fromIterable(list);
    }

    private record Book(long id, String title, long authorId) {
    }

    private record BookGenre(long bookId, long genreId) {
    }

}
