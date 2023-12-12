package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenreRepositoryJdbc implements GenreRepository {

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public List<Genre> findAll() {
        return jdbc.query("SELECT id, name FROM genres", new GnreRowMapper());
    }

    @Override
    public List<Genre> findAllByIds(List<Long> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", ids);

        return jdbc.query(
                "SELECT id, name FROM genres WHERE id IN (:ids)",
                params,
                new GnreRowMapper()
        );
    }

    public List<Genre> findBookGenres(long bookId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("book_id", bookId);

        return jdbc.query("""
                        SELECT g.id
                             , g.name
                          FROM genres              g
                          INNER JOIN books_genres bg ON bg.genre_id = g.id
                         WHERE bg.book_id = :book_id
                        """,
                params,
                new GnreRowMapper()
        );
    }

    private static class GnreRowMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            return new Genre(id, name);
        }
    }
}
