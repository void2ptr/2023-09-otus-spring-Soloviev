package ru.otus.hw.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Genre;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
public class BookMapper {

    public static BookDto toDto(ru.otus.hw.model.Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                null,
                null
        );
    }

    public static ru.otus.hw.model.Book toBook(BookDto bookDto) {
        return new ru.otus.hw.model.Book(
                bookDto.getId(),
                bookDto.getTitle(),
                bookDto.getAuthor().getId()
        );
    }

    public static Author toAuthor(BookDto bookDto) {
        return new Author(
                bookDto.getAuthor().getId(),
                bookDto.getAuthor().getFullName()
        );
    }

    public static List<Genre> toGenre(BookDto bookDto) {
        return bookDto.getGenres()
                .stream()
                .map(genreDto -> new Genre(genreDto.getId(), genreDto.getName()))
                .collect(Collectors.toList());
    }


}
