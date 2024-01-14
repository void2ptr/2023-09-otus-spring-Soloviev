package ru.otus.hw.data;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Comment;
import ru.otus.hw.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class InitTestData {
//    @Autowired
//    private final NamedParameterJdbcTemplate jdbc;

    public List<Author> getDbAuthors() {
//        return jdbc.query("SELECT id, full_name FROM authors", new AuthorRowMapper());

        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    public List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    public List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

    public List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

//    public List<Comment> getDbComments() {
//
//        return jdbc.query("SELECT id, name FROM genres", new CommentRowMapper());
//
//        AtomicLong commentId  = new AtomicLong(0);
//        return getDbBooks().stream()
//                .map(book ->
//                    IntStream.range(1, 4).boxed()
//                            .map(id -> {
//                                commentId.getAndIncrement();
//                                return new Comment(commentId.get(), "Comment_" + id, book);
//                            })
//                            .toList()
//                ).flatMap(List::stream).toList();
//    }


//    private static class CommentRowMapper implements RowMapper<Comment> {
//        @Override
//        public Comment mapRow(ResultSet rs, int i) throws SQLException {
//            long id = rs.getLong("id");
//            String name = rs.getString("name");
//            long bookId = rs.getLong("book_id");
//            return new Comment(id, name, bookId);
//        }
//    }
    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("full_name");
            return new Author(id, name);
        }
    }

}
