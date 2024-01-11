package ru.otus.hw.data;


import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.LongStream;


public class InitTestData {

    public static List<AuthorDto> getDbAuthors() {
        return LongStream.range(1L, 4L).boxed()
                .map(id -> new AuthorDto(id, "Author_" + id))
                .toList();
    }

    public static List<GenreDto> getDbGenres() {
        return LongStream.range(1L, 7L).boxed()
                .map(id -> new GenreDto(id, "Genre_" + id))
                .toList();
    }

    public static List<BookDto> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

    public static List<BookDto> getDbBooks(List<AuthorDto> dbAuthors, List<GenreDto> dbGenres) {
        return LongStream.range(1L, 4L).boxed()
                .map(id -> new BookDto(id,
                        "BookTitle_" + id,
                        dbAuthors.get((int)(id - 1)),
                        dbGenres.subList((int)(id - 1) * 2, (int)(id - 1) * 2 + 2)
                ))
                .toList();
    }

    public static List<CommentDto> getDbComments() {
        AtomicLong atomicLong  = new AtomicLong(0);
        return getDbBooks().stream()
                .map(book ->
                        LongStream.range(1L, 4L).boxed()
                            .map(id -> {
                                atomicLong.getAndIncrement();
                                return new CommentDto(atomicLong.get(), "Comment_" + id, book);
                            })
                            .toList()
                ).flatMap(List::stream).toList();
    }

}
