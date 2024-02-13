package ru.otus.hw.mongock.changelog;


import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;

@ChangeLog(order = "000")
public class DatabaseChangeCreateDb {

    @ChangeSet(order = "000", id = "dropDb", author = "solo", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
        db.createCollection("authors");
        db.createCollection("genres");
        db.createCollection("books");
        db.createCollection("comments");
    }

}
