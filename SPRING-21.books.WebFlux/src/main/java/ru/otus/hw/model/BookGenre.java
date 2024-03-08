package ru.otus.hw.model;


import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@ToString
@Table(name = "book_genre")
public class BookGenre {
    @Id
    private Long id;

    private final Long bookId;

    private final Long genreId;

    @PersistenceCreator
    public BookGenre(Long bookId, Long genreId) {
        this.bookId = bookId;
        this.genreId = genreId;
    }

}
