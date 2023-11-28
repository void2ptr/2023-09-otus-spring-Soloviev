package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;

import java.util.ArrayList;
import java.util.List;

@ChangeLog(order = "001")
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "solo", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "solo")
    public void insertAuthors(MongoDatabase db) {
        MongoCollection<Document> authorsCollection = db.getCollection("authors");
        List<Document> authors = new ArrayList<>();
        for (int i = 1; i < 4; ++i) {
            var doc = new Document()
                    .append("id", i)
                    .append("fullName", "Author_" + i);
            authors.add(doc);
            authorsCollection.insertOne(doc);
        }
//        authorsCollection.insertMany(authors);
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "solo")
    public void insertGenres(MongoDatabase db) {
        MongoCollection<Document> genresCollection = db.getCollection("genres");
        List<Document> books = new ArrayList<>();
        for (int i = 1; i < 7; ++i) {
            var doc = new Document()
                    .append("id", i)
                    .append("name", "Genre_"+ i);
            books.add(doc);
        }
        genresCollection.insertMany(books);
    }


    @ChangeSet(order = "004", id = "insertBooks", author = "solo")
    public void insertBooks(MongoDatabase db) {
        MongoCollection<Document> booksCollection = db.getCollection("books");
        List<Document> books = new ArrayList<>();
        int genreId = 0;
        for (int i = 1; i < 4; ++i) {
            var doc = new Document()
                    .append("id", i)
                    .append("title", "BookTitle_"+ i)
                    .append("author", i)
                    .append("genres", List.of(++genreId, ++genreId));
            books.add(doc);
        }
        booksCollection.insertMany(books);
    }


    @ChangeSet(order = "005", id = "insertComments", author = "solo")
    public void insertComments(MongoDatabase db) {
        MongoCollection<Document> genresCollection = db.getCollection("comments");
        List<Document> books = new ArrayList<>();
        int bookId = 1;
        for (int i = 1; i < 7; ++i) {
            var doc = new Document()
                    .append("id", i)
                    .append("description", "Comment_"+ i)
                    .append("book", bookId);
            if (i % 2 == 0) {
                ++bookId;
            }
            books.add(doc);
        }
        genresCollection.insertMany(books);
    }

//    @ChangeSet(order = "004", id = "insertBooks", author = "solo")
//    public void insertBook(BookRepository repository) {
//        List<Genre> genres = new ArrayList<>();
//        genres.add(new Genre(1, "Genre_1"));
//        genres.add(new Genre(2, "Genre_2"));
//        repository.save(new Book(1, "BookTitle_1", 1, "Author_1", genres));
//    }
}
