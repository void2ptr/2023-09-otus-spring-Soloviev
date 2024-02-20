package ru.otus.hw.model.mongo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@SuppressWarnings("unused")
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "authors")
public class MongoAuthor {
    @Id
    private String id;

    @Indexed
    private String fullName;


    public MongoAuthor(String fullName) {
        this.id = ObjectId.get().toString();
        this.fullName = fullName;
    }
}
