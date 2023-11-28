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
@Document(collection = "comments")
public class Comment {
    @Id
    private BigInteger _id;

    @Indexed
    @Field(name = "id")
    private long id;

    @Field(name = "description")
    private String description;

    @Field(name = "book")
    private Book book;

    public Comment(long id, String description, Book book) {
        this.id = id;
        this.description = description;
        this.book = book;
    }
}
