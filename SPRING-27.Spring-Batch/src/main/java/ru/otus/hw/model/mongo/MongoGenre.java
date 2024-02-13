package ru.otus.hw.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "genres")
public class MongoGenre {
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    @Indexed
    private String externalLink;

    public MongoGenre(String name, String externalLink) {
        this.id = ObjectId.get().toString();
        this.name = name;
        this.externalLink = externalLink;
    }
}
