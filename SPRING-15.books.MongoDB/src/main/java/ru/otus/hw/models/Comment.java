package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "comments")
public class Comment {
    @Id
    private String id;

    @Indexed(unique = true)
    private String title;

    @DBRef
    private Book book;

    private List<String> notes;

    public Comment(Book book, List<String> notes) {
        this.title = book.getTitle();
        this.book = book;
        this.notes = notes;
    }

    public Comment(Book book, String... notes) {
        this.title = book.getTitle();
        this.book = book;
        this.notes = new ArrayList<>(List.of(notes));
    }

    public void addNote(String note) {
        this.notes.add(note);
    }

    public void updateNote(int id, String note) {
        if (this.notes.size() > id) {
            this.notes.set(id, note);
        }
    }

    public void deleteNote(int id) {
        if (this.notes.size() > id) {
            this.notes.remove(id);
        }
    }
}
