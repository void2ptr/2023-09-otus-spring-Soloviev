package ru.otus.hw.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookIdsDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.model.Book;

import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
public class BookMapper {

    public static BookDto toDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                AuthorMapper.toDto(book.getAuthor()),
                book.getGenres()
                        .stream()
                        .map(g -> new GenreDto(g.getId(), g.getName()))
                        .collect(Collectors.toList())
        );
    }

    public static Book toBook(BookDto bookDto) {
        return new Book(
                bookDto.getId(),
                bookDto.getTitle(),
                AuthorMapper.toAuthor(bookDto.getAuthor()),
                bookDto.getGenres().stream()
                        .map(GenreMapper::toGenre)
                        .collect(Collectors.toList())
        );
    }

    public static byte[] toFormUrlEncoded(BookIdsDto bookDto) {
        StringBuilder result = new StringBuilder();
        result.append("id=").append(bookDto.getId());
        result.append("&").append("title=").append(bookDto.getTitle().replace(" ", "+"));
        result.append("&").append("authorId=").append(bookDto.getAuthorId());
        bookDto.getGenresId().forEach(genreId -> result.append("&").append("genresId=").append(genreId));
        return result.toString().getBytes();
    }
}
