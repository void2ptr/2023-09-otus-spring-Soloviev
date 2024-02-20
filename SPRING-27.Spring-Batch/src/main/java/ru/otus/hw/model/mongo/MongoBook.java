package ru.otus.hw.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@SuppressWarnings("unused")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "books")
public class MongoBook {
    @Id
    private String id;

    @Indexed(unique = true)
    private String title;

    private List<MongoAuthor> authors;

    private List<MongoGenre> genres;

    public MongoBook(String title, List<MongoAuthor> authors, List<MongoGenre> genres) {
        this.id = ObjectId.get().toString();
        this.title = title;
        this.authors = authors;
        this.genres = genres;
    }
}
