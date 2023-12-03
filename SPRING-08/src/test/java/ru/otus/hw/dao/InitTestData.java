package ru.otus.hw.dao;

import com.google.gson.reflect.TypeToken;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.lang.reflect.Type;
import java.util.List;


public class InitTestData {

    private static List<Author> authors = null;
    private static List<Book> books = null;
    private static List<Genre> genres = null;
    private static List<Comment> comments = null;


    public static List<Author> getAuthors() {
        if (authors == null) {
            Type listType = new TypeToken<List<Author>>() {}.getType();
            authors = JsonFileReaderDao.readCollectionFromFile("data/authors.json", listType);
        }
        return authors;
    }

    public static List<Book> getDbBooks() {
        if (books == null) {
            Type listType = new TypeToken<List<Book>>() {}.getType();
            books = JsonFileReaderDao.readCollectionFromFile("data/books.json", listType);
        }
        return books;
    }

    public static List<Genre> getDbGenres() {
        if (genres == null) {
            Type listType = new TypeToken<List<Genre>>() {}.getType();
            genres =  JsonFileReaderDao.readCollectionFromFile("data/genres.json", listType);
        }
        return genres;
    }

    public static List<Comment> getDbComments() {
        if (comments == null) {
            Type listType = new TypeToken<List<Comment>>() {}.getType();
            comments = JsonFileReaderDao.readCollectionFromFile("data/comments.json", listType);
        }
        return comments;
    }
}
