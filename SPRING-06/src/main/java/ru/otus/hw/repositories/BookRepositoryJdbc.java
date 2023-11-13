package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
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
import java.util.*;

@Repository
@RequiredArgsConstructor
public class BookRepositoryJdbc implements BookRepository {

    private final NamedParameterJdbcTemplate jdbc;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @Override
    public Optional<Book> findById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        List<BookRow> bookRows = jdbc.query("""                        
                        SELECT b.id        as id
                             , b.title     AS title
                             , a.id        AS author_id
                             , a.full_name AS author_name
                          FROM books         b
                         INNER JOIN authors a ON a.id = b.author_id
                         WHERE b.id = :id
                        """,
                params,
                new BookRowMapper()
        );
        List<Book> books = new ArrayList<>();
        bookRows.forEach(bookRow -> {
            Book book = new Book(bookRow.id(), bookRow.title(), new Author(bookRow.authorId(),bookRow.authorName()),
                    genreRepository.findBookGenres(bookRow.id())
            );
            books.add(book);
        });
        return books.stream().findFirst();
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
        //...
        Optional<Book> bookOptional = findById(id);

        if (bookOptional.isPresent()) {
            removeGenresRelationsFor(bookOptional.get());

            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("id", id);
            jdbc.update("DELETE FROM books WHERE id = :id", params);
        }
    }

    private List<Book> getAllBooksWithoutGenres() {
        List<Book> books = new ArrayList<>();

        jdbc.query("""                        
                        SELECT b.id        as id
                             , b.title     AS title
                             , a.id        AS author_id
                             , a.full_name AS author_name
                        FROM books         b
                        INNER JOIN authors a ON a.id = b.author_id
                        """,
                new BookRowMapper()
        ).forEach(bookRow -> books.add(new Book(bookRow.id(), bookRow.title(), new Author(bookRow.authorId(),
                bookRow.authorName()),
                new ArrayList<>()
        )));

        return books;
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return jdbc.query("SELECT book_id, genre_id FROM books_genres", new BookGenreRowMapper());
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres,
                                List<Genre> genres,
                                List<BookGenreRelation> relations) {
        // Добавить книгам (booksWithoutGenres) жанры (genres) в соответствии со связями (relations)
       booksWithoutGenres.stream()
                .map(booksWithoutGenre -> {
                    List<Genre> bookGenres = new ArrayList<>();
                    relations.stream()
                            .filter(relation -> relation.bookId() == booksWithoutGenre.getId())
                            .map(relation -> {
                                List<Genre> relationGenres = genres.stream()
                                        .filter(genre -> genre.getId() == relation.genreId())
                                        .map(genre -> {
                                            bookGenres.add(genre);
                                            return genre;
                                        }).toList();
                                return relationGenres;
                            }).toList();
                    booksWithoutGenre.setGenres(bookGenres);
                    return booksWithoutGenre;
                }).toList();
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

        return freshBook.orElseGet(Book::new);
    }


    private void batchInsertGenresRelationsFor(Book book) {
        // batchUpdate
        List<Genre> genres = book.getGenres();

        jdbc.getJdbcTemplate().batchUpdate(
                "insert into books_genres(book_id, genre_id) values (?, ?)",
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
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

    private record BookRow(long id, String title, long authorId, String authorName) {
    }

    @RequiredArgsConstructor
    private static class BookRowMapper implements RowMapper<BookRow> {

        @Override
        public BookRow mapRow(ResultSet rs, int rowNum) throws SQLException {

            return new BookRow(
                    rs.getLong("id"),
                    rs.getString("title"),
                    rs.getLong("author_id"),
                    rs.getString("author_name")
            );
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
