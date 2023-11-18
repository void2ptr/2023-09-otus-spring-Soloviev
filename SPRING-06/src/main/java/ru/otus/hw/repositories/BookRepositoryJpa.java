package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
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
public class BookRepositoryJpa implements BookRepository {

    private final NamedParameterJdbcTemplate jdbc;

    @PersistenceContext
    private EntityManager em;

    private final GenreRepository genreRepository;

    @Override
    public Optional<Book> findById(long id) {
        TypedQuery<Book> query = em.createQuery("""
                select b
                  from Book b
                  left join fetch b.author a
                  left join fetch b.genres g
                 where b.id = :id
                """,
                Book.class);
        query.setParameter("id", id);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public List<Book> findAll() {
        TypedQuery<Book> query = em.createQuery("""
                select b
                  from Book b
                  left join fetch b.author a
                  left join fetch b.genres g
                """,
                Book.class);
        return query.getResultList();
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
