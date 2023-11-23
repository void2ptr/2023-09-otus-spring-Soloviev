package ru.otus.hw.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

//@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "comments")
@NamedEntityGraph(name = "comment-book-entity-graph",
        attributeNodes = {@NamedAttributeNode("book")})
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "description", nullable = false)
    private String description;

    @Fetch(FetchMode.JOIN)
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)
    private Book book;

}
