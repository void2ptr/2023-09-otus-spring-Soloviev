package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        List<BookRow> bookRows = jdbc.query("SELECT id, title, author_id FROM books WHERE id = :id",
                params,
                new BookRowMapper()
        );

        List<Book> books = new ArrayList<>();
        bookRows.forEach(bookRow -> {
            Optional<Author> author = authorRepository.findById(bookRow.authorId());

            Book book = new Book(
                    bookRow.id(),
                    bookRow.title(),
                    author.orElseGet(Author::new),
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
            Book book = bookOptional.get();

            removeGenresRelationsFor(book);

            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("id", book.getId());
            jdbc.update("DELETE FROM books WHERE id = :id", params);
        }
    }

    private List<Book> getAllBooksWithoutGenres() {
        List<Book> books = new ArrayList<>();

        List<BookRow> bookRows = jdbc.query("SELECT id, title, author_id FROM books",
                new BookRowMapper()
        );

        bookRows.forEach(bookRow -> {
            Optional<Author> author = authorRepository.findById(bookRow.authorId());
            books.add(new Book(
                    bookRow.id(),
                    bookRow.title(),
                    author.orElseGet(Author::new),
                    new ArrayList<>()
            ));
        });

        return books;
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return jdbc.query("SELECT book_id, genre_id FROM books_genres", new BookGenreRowMapper());
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres,
                                List<Genre> genres,
                                List<BookGenreRelation> relations) {
        // Добавить книгам (booksWithoutGenres) жанры (genres) в соответствии со связями (relations)
        booksWithoutGenres.forEach(book -> {
            List<Genre> genresA = new ArrayList<>();
            relations.stream()
                    .filter(relation -> relation.bookId == book.getId())
                    .forEach(relation -> genres.stream()
                            .filter(genre -> genre.getId() == relation.genreId())
                            .forEach(genresA::add));
            book.setGenres(genresA);
        });
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
        for (Genre genre : book.getGenres()) {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("book_id", book.getId());
            params.addValue("genre_id", genre.getId());
            jdbc.update("insert into books_genres(book_id, genre_id) values (:book_id, :genre_id)", params);
        }
    }

    private void removeGenresRelationsFor(Book book) {
        //...
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("book_id", book.getId());
        jdbc.update("DELETE FROM books_genres WHERE book_id = :book_id", params);
    }

    private record BookRow(long id, String title, long authorId) {
    }

    @RequiredArgsConstructor
    private static class BookRowMapper implements RowMapper<BookRow> {

        @Override
        public BookRow mapRow(ResultSet rs, int rowNum) throws SQLException {

            long id = rs.getLong("id");
            String title = rs.getString("title");
            long authorId = rs.getLong("author_id");

            return new BookRow(id, title, authorId);
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<List<Book>> {

        @Override
        public List<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
            return new ArrayList<>();
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
