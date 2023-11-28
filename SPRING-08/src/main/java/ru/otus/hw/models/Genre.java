package ru.otus.hw.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "genres")
public class Genre {
    @Id
    private BigInteger _id;

    @Indexed
    @Field(name = "id")
    private long id;

    @Field(name = "name")
    private String name;

    public Genre(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
