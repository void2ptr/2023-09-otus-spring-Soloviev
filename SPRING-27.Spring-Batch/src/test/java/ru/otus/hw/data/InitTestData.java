package ru.otus.hw.data;


import ru.otus.hw.model.mongo.MongoAuthor;
import ru.otus.hw.model.mongo.MongoBook;
import ru.otus.hw.model.mongo.MongoComment;
import ru.otus.hw.model.mongo.MongoGenre;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class InitTestData {

    public static List<MongoAuthor> getDbAuthors() {
        return LongStream.range(1L, 4L).boxed()
                .map(id -> new MongoAuthor("Author_" + id, id.toString()))
                .toList();
    }

    public static List<MongoGenre> getDbGenres() {
        return LongStream.range(1L, 7L).boxed()
                .map(id -> new MongoGenre("Genre_" + id, id.toString()))
                .toList();
    }

    public static List<MongoBook> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

    public static List<MongoBook> getDbBooks(List<MongoAuthor> dbAuthors, List<MongoGenre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new MongoBook(
                        "BookTitle_" + id,
                        List.of(dbAuthors.get(id - 1)),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2),
                        id.toString()
                ))
                .toList();
    }

    public static List<MongoComment> getDbComments() {
        AtomicLong commentId  = new AtomicLong(0);
        return getDbBooks().stream()
                .map(book ->
                        IntStream.range(1, 4).boxed()
                                .map(id -> {
                                    commentId.getAndIncrement();
                                    return new MongoComment("Comment_" + id, book, id.toString());
                                })
                                .toList()
                ).flatMap(List::stream).toList();
    }

}

