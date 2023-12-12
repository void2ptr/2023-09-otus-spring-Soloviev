package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class BookDto {

    private long id;

    private String title;

    private Author author;

    private List<Genre> genres;

    public static BookDto toDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenres()
                        .stream()
                        .map(g -> new Genre(g.getId(), g.getName()))
                        .toList()
        );
    }

    // for future use
    public static Book toBook(BookDto bookDto) {
        return new Book(
                bookDto.getId(),
                bookDto.getTitle(),
                bookDto.getAuthor(),
                bookDto.getGenres()
        );
    }
}
