package ru.otus.hw.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.model.BookModel;

import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
public class BookMapper {

    public static BookDto toDto(BookModel bookModel) {
        return new BookDto(
                bookModel.getId(),
                bookModel.getTitle(),
                AuthorMapper.toDto(bookModel.getAuthor()),
                bookModel.getGenres()
                        .stream()
                        .map(g -> new GenreDto(g.getId(), g.getName()))
                        .collect(Collectors.toList())
        );
    }

    public static BookModel toBook(BookDto bookDto) {
        return new BookModel(
                bookDto.getId(),
                bookDto.getTitle(),
                AuthorMapper.toAuthor(bookDto.getAuthor()),
                bookDto.getGenres().stream()
                        .map(GenreMapper::toGenre)
                        .collect(Collectors.toList())
        );
    }
}
