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
@Document(collection = "authors")
public class Author {
    @Id
    private BigInteger _id;

    @Indexed
    @Field(name = "id")
    private long id;

    @Field(name = "fullName")
    private String fullName;

    public Author(Integer id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }
}
