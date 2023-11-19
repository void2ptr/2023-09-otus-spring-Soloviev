package ru.otus.hw.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books_genres")
public class BookGenre {
    @Id
    @Column(name = "book_id", nullable = false)
    private long bookId;

    @Id
    @Column(name = "genre_id", nullable = false)
    private String genreId;
}
