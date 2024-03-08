package ru.otus.hw.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.spi.Row;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;

import java.io.IOException;
import java.util.List;
import java.util.function.BiFunction;

public class BookR2RowMapper implements BiFunction<Row, Object, BookDto> {

    private final ObjectMapper objectMapper;

    public BookR2RowMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public BookDto apply(Row row, Object o) {
        try {
            Long id = row.get("book_id", Long.class);
            String title = row.get("book_title", String.class);
            String authorStr = row.get("author", String.class);
            String genresStr = row.get("genres", String.class);

            AuthorDto authorDto = objectMapper.readValue(authorStr, AuthorDto.class);
            List<GenreDto> genresDto = objectMapper.readValue(genresStr, new TypeReference<>(){});

            return new BookDto(id, title, authorDto, genresDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
