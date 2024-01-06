package ru.otus.hw.data;

import ru.otus.hw.model.Author;
import ru.otus.hw.model.BookModel;
import ru.otus.hw.model.Comment;
import ru.otus.hw.model.Genre;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.LongStream;

public class InitTestData {

    public static List<Author> getDbAuthors() {
        return LongStream.range(1L, 4L).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    public static List<Genre> getDbGenres() {
        return LongStream.range(1L, 7L).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    public static List<BookModel> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

    public static List<BookModel> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return LongStream.range(1L, 4L).boxed()
                .map(id -> new BookModel(id,
                        "BookTitle_" + id,
                        dbAuthors.get((int)(id - 1)),
                        dbGenres.subList((int)(id - 1) * 2, (int)(id - 1) * 2 + 2)
                ))
                .toList();
    }

    public static List<Comment> getDbComments() {
        AtomicLong commentId  = new AtomicLong(0);
        return getDbBooks().stream()
                .map(book ->
                        LongStream.range(1L, 4L).boxed()
                            .map(id -> {
                                commentId.getAndIncrement();
                                return new Comment(commentId.get(), "Comment_" + id, book);
                            })
                            .toList()
                ).flatMap(List::stream).toList();
    }

}
