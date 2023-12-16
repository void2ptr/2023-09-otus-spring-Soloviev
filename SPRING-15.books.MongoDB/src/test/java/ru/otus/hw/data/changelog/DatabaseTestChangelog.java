package ru.otus.hw.data.changelog;


import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.data.InitTestData;

import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

@ChangeLog(order = "000")
public class DatabaseTestChangelog {

    @ChangeSet(order = "000", id = "dropDb", author = "solo", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "001", id = "insertGenres", author = "solo")
    public void insertGenres(GenreRepository repository) {
        InitTestData.getDbGenres().forEach(repository::save);
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "solo")
    public void insertAuthors(AuthorRepository repository) {
        InitTestData.getDbAuthors().forEach(repository::save);
    }

    @ChangeSet(order = "003", id = "insertBooks", author = "solo")
    public void insertBooks(BookRepository repository) {
        InitTestData.getDbBooks().forEach(repository::save);
    }

    @ChangeSet(order = "004", id = "insertComments", author = "solo")
    public void insertComments(CommentRepository repository) {
        InitTestData.getDbComments().forEach(repository::save);
    }

}
