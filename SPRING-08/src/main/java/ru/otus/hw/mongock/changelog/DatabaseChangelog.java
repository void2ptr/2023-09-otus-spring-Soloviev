package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.spring.v5.EnableMongock;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "solo", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthor", author = "solo")
    public void insertAuthors(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection("authors");
        var doc = new Document()
                .append("id", 1)
                .append("fullName", "Lermontov");
        myCollection.insertOne(doc);
    }

//    @ChangeSet(order = "003", id = "insertPushkin", author = "stvort")
//    public void insertPushkin(PersonRepository repository) {
//        repository.save(new Person("Pushkin"));
//    }
}
