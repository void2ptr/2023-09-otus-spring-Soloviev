package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "comments")
public class Comment {
    @Id
    private String id;

    @Indexed
    @DBRef
    private Book book;

    private List<String> comment;

    public Comment(Book book, List<String> comment) {
        this.book = book;
        this.comment = comment;
    }

    public Comment(String id, Book book, String... comment) {
        this.id = id;
        this.book = book;
        this.comment = List.of(comment);
    }

}
