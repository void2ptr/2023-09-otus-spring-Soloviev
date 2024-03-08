package ru.otus.hw.model;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;


@Getter
@ToString
@Table(name = "author")
public class Author {
    @Id
    private Long id;

    private final String fullName;

    @PersistenceCreator
    public Author(Long id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public Author(String fullName) {
        this.fullName = fullName;
    }
}
