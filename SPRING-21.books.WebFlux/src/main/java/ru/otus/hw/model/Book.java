package ru.otus.hw.model;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;


@Getter
@ToString
@Table(name = "book")
public class Book {
    @Id
    private Long id;

    private final String title;

    private final Long authorId;

    @PersistenceCreator
    public Book(Long id, String title, Long authorId) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
    }

    public Book(String title, Long authorId) {
        this.title = title;
        this.authorId = authorId;
    }

}
