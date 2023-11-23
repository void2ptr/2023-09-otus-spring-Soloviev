package ru.otus.hw.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

//@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "books")
// Позволяет указать какие связи родительской сущности загружать в одном с ней запросе
@NamedEntityGraph(name = "book-author-entity-graph",
        attributeNodes = {@NamedAttributeNode("author")})
@NamedEntityGraph(name = "book-genres-entity-graph",
        attributeNodes = {@NamedAttributeNode("genres")})
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Fetch(FetchMode.JOIN)
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private Author author;

    // Все данные талицы будут загружены в память отдельным запросом и соединены с родительской сущностью
    @Fetch(FetchMode.SUBSELECT)
//    @BatchSize(size = 5)
    @ManyToMany(targetEntity = Genre.class, cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "books_genres",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id"))
    private List<Genre> genres;
}
