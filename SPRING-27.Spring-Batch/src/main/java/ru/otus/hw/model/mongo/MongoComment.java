package ru.otus.hw.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "comments")
public class MongoComment {
    @Id
    private String id;

    @Indexed(unique = true)
    private String description;

    @DBRef
    private MongoBook mongoBook;

    @Indexed
    private String externalLink;

    public MongoComment(String description, MongoBook mongoBook, String externalLink) {
        this.id = ObjectId.get().toString();
        this.description = description;
        this.mongoBook = mongoBook;
        this.externalLink = externalLink;
    }
}
