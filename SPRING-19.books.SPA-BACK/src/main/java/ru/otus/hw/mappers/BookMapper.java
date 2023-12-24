package ru.otus.hw.mappers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
public class BookMapper {

    public static BookDto toDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenres()
                        .stream()
                        .map(g -> new Genre(g.getId(), g.getName()))
                        .collect(Collectors.toList())
        );
    }

    public static Book toBook(BookDto bookDto) {
        return new Book(
                bookDto.getId(),
                bookDto.getTitle(),
                bookDto.getAuthor(),
                bookDto.getGenres()
        );
    }

    public static byte[] toFormUrlEncoded(BookDto bookDto) {
        StringBuilder result = new StringBuilder();
        result.append("id=").append(bookDto.getId());
        result.append("&").append("title=").append(bookDto.getTitle().replace(" ", "+"));
        result.append("&").append("authorId=").append(bookDto.getAuthor().getId());
        bookDto.getGenres().forEach(genre -> result.append("&").append("genresId=").append(genre.getId()));
        return result.toString().getBytes();
    }
}
