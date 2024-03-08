package ru.otus.hw.model;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@ToString
@Table(name = "comment")
public class Comment {
    @Id
    private Long id;

    private final Long bookId;

    private final String description;

    @PersistenceCreator
    public Comment(Long id, Long bookId, String description) {
        this.id = id;
        this.bookId = bookId;
        this.description = description;
    }

    public Comment(Long bookId, String description) {
        this.bookId = bookId;
        this.description = description;
    }

}
