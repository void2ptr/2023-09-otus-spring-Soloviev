package ru.otus.hw.model;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@ToString
@Table(name = "genre")
public class Genre {
    @Id
    private Long id;

    private final String name;

    @PersistenceCreator
    public Genre(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @PersistenceCreator
    public Genre(String name) {
        this.name = name;
    }

}
