package ru.otus.hw.repositories;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BookRepositoryJdbc implements BookRepository {

    private final NamedParameterJdbcTemplate jdbc;

    private final GenreRepository genreRepository;

    @Override
    public Optional<Book> findById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        List<Book> books = jdbc.query("""                        
                        SELECT b.id        as book_id
                             , b.title     AS book_title
                             , a.id        AS author_id
                             , a.full_name AS author_name
                             , g.id        AS genre_id
                             , g.name      AS genre_name
                          FROM      books         b
                         INNER JOIN authors       a ON a.id       = b.author_id
                         INNER JOIN books_genres bg ON bg.book_id = b.id
                         INNER JOIN genres        g ON g.id       = bg.genre_id
                         WHERE b.id = :id
                        """,
                params,
                new BookResultSetExtractor()
        );
        return books != null ? books.stream().findFirst() : Optional.empty();
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        jdbc.update("DELETE FROM books WHERE id = :id", params);
    }

    private List<Book> getAllBooksWithoutGenres() {
        return jdbc.query("""                        
                        SELECT b.id        as book_id
                             , b.title     AS book_title
                             , a.id        AS author_id
                             , a.full_name AS author_name
                          FROM      books         b
                         INNER JOIN authors       a ON a.id       = b.author_id
                        """,
                new BooksWithoutGenresResultSetExtractor()
        );
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return jdbc.query("SELECT book_id, genre_id FROM books_genres", new BookGenreRowMapper());
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres,
                                List<Genre> genres,
                                List<BookGenreRelation> relations) {
        // Добавить книгам (booksWithoutGenres) жанры (genres) в соответствии со связями (relations)
        Map<Long, Book> mapBooks = booksWithoutGenres.stream()
                .collect(Collectors.toMap(Book::getId, book -> book, (a, b) -> b));
        Map<Long, Genre> mapGenre = genres.stream()
                .collect(Collectors.toMap(Genre::getId, genre -> genre, (a, b) -> b));

        relations.forEach(relation -> mapBooks.get(relation.bookId())
                .getGenres().add(mapGenre.get(relation.genreId())));
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        //...
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());
        jdbc.update("insert into books(title, author_id) values (:title, :author_id)",
                params,
                keyHolder,
                new String[]{"id"}
        );

        // noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        //...
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());
        params.addValue("id", book.getId());
        jdbc.update("""
                        update books
                           set title     = :title
                             , author_id = :author_id
                         where id        = :id
                        """,
                params
        );

        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        Optional<Book> freshBook = findById(book.getId());

        return freshBook.orElse(null);
    }


    private void batchInsertGenresRelationsFor(Book book) {
        // batchUpdate
        List<Genre> genres = book.getGenres();

        jdbc.getJdbcTemplate().batchUpdate(
                "insert into books_genres(book_id, genre_id) values (?, ?)",
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, book.getId());
                        ps.setLong(2, genres.get(i).getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return genres.size();
                    }
                });
    }

    private void removeGenresRelationsFor(Book book) {
        //...
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("book_id", book.getId());
        jdbc.update("DELETE FROM books_genres WHERE book_id = :book_id", params);
    }

    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<List<Book>> {
        @Override
        public List<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, Book> map = new HashMap<>();
            while (rs.next()) {
                long bookId = rs.getLong("book_id");
                Book book = map.get(bookId);
                if (book == null) {
                    book = new Book(
                            bookId,
                            rs.getString("book_title"),
                            new Author(rs.getLong("author_id"),rs.getString("author_name")),
                            new ArrayList<>(List.of(new Genre(rs.getLong("genre_id"),rs.getString("genre_name"))))
                    );
                    map.put(book.getId(), book);
                } else {
                    book.getGenres().add(new Genre(rs.getLong("genre_id"), rs.getString("genre_name")));
                }
            }
            return new ArrayList<>(map.values());
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class BooksWithoutGenresResultSetExtractor implements ResultSetExtractor<List<Book>> {
        @Override
        public List<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, Book> map = new HashMap<>();
            while (rs.next()) {
                long bookId = rs.getLong("book_id");
                Book book = map.get(bookId);
                if (book == null) {
                    book = new Book(
                            bookId,
                            rs.getString("book_title"),
                            new Author(rs.getLong("author_id"),rs.getString("author_name")),
                            new ArrayList<>()
                    );
                    map.put(book.getId(), book);
                }
            }
            return new ArrayList<>(map.values());
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }

    private static class BookGenreRowMapper implements RowMapper<BookGenreRelation> {

        @Override
        public BookGenreRelation mapRow(ResultSet rs, int i) throws SQLException {
            long bookId = rs.getLong("book_id");
            long genreId = rs.getLong("genre_id");
            return new BookGenreRelation(bookId, genreId);
        }
    }
}
