package ru.otus.hw.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Getter
@Setter
@Table(name = "book")
public class BookModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Author author;

    private List<Genre> genres;

    @PersistenceCreator
    public BookModel(Long id, String title, Author author, List<Genre> genres) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genres = genres;
    }

    public BookModel(String title, Author author, List<Genre> genres) {
        this.title = title;
        this.author = author;
        this.genres = genres;
    }
}
