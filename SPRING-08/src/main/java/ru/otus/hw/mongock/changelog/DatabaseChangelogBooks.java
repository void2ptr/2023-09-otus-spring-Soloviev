package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ChangeLog(order = "000")
public class DatabaseChangelogBooks {

    private Map<String, Genre> genres = new HashMap<>();
    private Map<String, Author> authors = new HashMap<>();

    @ChangeSet(order = "000", id = "dropDb", author = "solo", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "001", id = "insertGenres", author = "solo")
    public void insertGenres(GenreRepository repository) {
        genres.put("видения", repository.save(new Genre("видения")));
        genres.put("новелла", repository.save(new Genre("новелла")));
        genres.put("ода", repository.save(new Genre("ода")));
        genres.put("опус", repository.save(new Genre("опус")));
        genres.put("очерк", repository.save(new Genre("очерк")));
        genres.put("поэма", repository.save(new Genre("поэма")));
        genres.put("повесть", repository.save(new Genre("повесть")));
        genres.put("пьеса", repository.save(new Genre("пьеса")));
        genres.put("рассказ", repository.save(new Genre("рассказ")));
        genres.put("роман", repository.save(new Genre("роман")));
        genres.put("скетч", repository.save(new Genre("скетч")));
        genres.put("эпопея", repository.save(new Genre("эпопея")));
        genres.put("эпос", repository.save(new Genre("эпос")));
        genres.put("эссе", repository.save(new Genre("эссе")));
        genres.put("комедия", repository.save(new Genre("комедия")));
        genres.put("эпиграммы", repository.save(new Genre("эпиграммы")));
        genres.put("хроника", repository.save(new Genre("хроника")));
        genres.put("мемуар", repository.save(new Genre("мемуар")));
        genres.put("военная проза", repository.save(new Genre("военной проза")));
        genres.put("учебник", repository.save(new Genre("учебник")));
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "solo")
    public void insertAuthors(AuthorRepository repository) {
        authors.put("Толстой", repository.save(new Author("Толстой Лев Николаевич")));
        authors.put("Тацит", repository.save(new Author("Публий Корнелий Тацит")));
        authors.put("Цезарь", repository.save(new Author("Цезарь Гай Юлий")));
        authors.put("Ливий", repository.save(new Author("Тит Ливий")));
        authors.put("Марцеллин", repository.save(new Author("Аммиан Марцеллин")));
        authors.put("Флавий", repository.save(new Author("Иосиф Флавий")));
        authors.put("Макиавелли", repository.save(new Author("Никколо Макиавелли")));
        authors.put("Аппулей", repository.save(new Author("Аппулей")));
        authors.put("Эзоп", repository.save(new Author("Эзоп")));
        authors.put("Аристофан", repository.save(new Author("Аристофан")));
        authors.put("Эсхил", repository.save(new Author("Эсхил")));
        authors.put("Марциал", repository.save(new Author("Марциал")));
    }

    @ChangeSet(order = "003", id = "insertBooks", author = "solo")
    public void insertBooks(BookRepository repository) {
        repository.save(new Book("Война и Мир",
                List.of(authors.get("Толстой")),
                List.of(genres.get("роман"), genres.get("эпос"))));
        repository.save(new Book("О происхождении, расположении, нравах и населении Германии",
                List.of(authors.get("Тацит")),
                List.of(genres.get("очерк"), genres.get("рассказ"))));
        repository.save(new Book("Записки о галльской войне",
                List.of(authors.get("Цезарь")),
                List.of(genres.get("хроника"), genres.get("мемуар"), genres.get("военная проза"))));
        repository.save(new Book("История Рима от основания Города",
                List.of(authors.get("Ливий")),
                List.of(genres.get("хроника"), genres.get("эпос"))));
        repository.save(new Book("Деяния",
                List.of(authors.get("Марцеллин")),
                List.of(genres.get("хроника"), genres.get("эпос"))));
        repository.save(new Book("Ватиканский кодекс",
                List.of(authors.get("Марцеллин")),
                List.of(genres.get("хроника"), genres.get("эпос"))));
        repository.save(new Book("Иудейская война",
                List.of(authors.get("Флавий")),
                List.of(genres.get("хроника"), genres.get("военная проза"))));
        repository.save(new Book("Иудейские древности",
                List.of(authors.get("Флавий")),
                List.of(genres.get("хроника"), genres.get("роман"))));
        repository.save(new Book("Государь",
                List.of(authors.get("Макиавелли")),
                List.of(genres.get("хроника"), genres.get("учебник"))));
        repository.save(new Book("Золотой осёл",
                List.of(authors.get("Аппулей")),
                List.of(genres.get("повесть"), genres.get("эпопея"))));
        repository.save(new Book("Басни",
                List.of(authors.get("Эзоп")),
                List.of(genres.get("учебник"), genres.get("новелла"))));
        repository.save(new Book("Птицы",
                List.of(authors.get("Аристофан")),
                List.of(genres.get("комедия"), genres.get("новелла"))));
        repository.save(new Book("Прометей",
                List.of(authors.get("Эсхил")),
                List.of(genres.get("пьеса"), genres.get("рассказ"))));
        repository.save(new Book("Зрелища",
                List.of(authors.get("Марциал")),
                List.of(genres.get("эпиграммы"))));
    }
}
