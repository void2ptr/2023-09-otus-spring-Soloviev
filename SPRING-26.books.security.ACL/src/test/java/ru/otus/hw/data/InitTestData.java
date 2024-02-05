package ru.otus.hw.data;


import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Comment;
import ru.otus.hw.model.Genre;
import ru.otus.hw.model.Role;
import ru.otus.hw.model.User;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;
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

    public static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

    public static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book((long)id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    public static List<Comment> getDbComments() {
        AtomicLong commentId  = new AtomicLong(0);
        return getDbBooks().stream()
                .map(book ->
                        IntStream.range(1, 4).boxed()
                                .map(id -> {
                                    commentId.getAndIncrement();
                                    return new Comment(commentId.get(), "Comment_" + id, book);
                                })
                                .toList()
                ).flatMap(List::stream).toList();
    }

    public static List<User> getDbUsers() {
        return List.of(
                new User("admin", "password",
                        true, true, true, true,
                        List.of(new Role("admin", "ADMIN"))),
                new User("user", "password",
                        true, true, true, true,
                        List.of(new Role("user", "USER"))));
    }

}

