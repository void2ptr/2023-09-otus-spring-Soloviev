package ru.otus.hw.data;

import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class InitTestData {
    private static final int AUTHOR_COUNT = 10; // количество авторов
    private static final int GENRE_COUNT = 10; // количество жанров
    private static final int BOOK_COUNT = 10; // количество книг
    private static final int RAND_BOUND = 1000000000; // граница генератора
    private static final int MAX_BOOK_AUTHORS = 5; // максимальное количество авторов у одной книги
    private static final int MAX_BOOK_GENRES = 5; // максимальное количество жанров у одной книги
    private static final int MAX_COMMENTS_NOTES = 100; // максимальное количество заметок в одном комментарии

    private static final List<Author> authors = new ArrayList<>();
    private static final List<Book> books = new ArrayList<>();
    private static final List<Genre> genres = new ArrayList<>();
    private static final List<Comment> comments = new ArrayList<>();

    public static List<Author> getDbAuthors() {
        if (authors.isEmpty()) {
            IntStream.range(0, AUTHOR_COUNT).forEach(i -> authors.add(authorRandomGenerator()));
        }
        return authors;
    }

    public static List<Book> getDbBooks() {
        if (books.isEmpty()) {
            IntStream.range(0, BOOK_COUNT).forEach(i -> books.add(bookRandomGenerator(
                    getDbAuthors(),
                    getDbGenres()
            )));
        }
        return books;
    }

    public static List<Genre> getDbGenres() {
        if (genres.isEmpty()) {
            IntStream.range(0, GENRE_COUNT).forEach(i -> genres.add(genreRandomGenerator()));
        }
        return genres;
    }

    public static List<Comment> getDbComments() {
        if (comments.isEmpty()) {
            books.forEach(book -> comments.add(commentRandomGenerator(book)));
        }
        return comments;
    }

//  ---------------------------------------------------------------------
    public static Author authorRandomGenerator() {
        SecureRandom random = getSecureRandom();
        Author author = new Author(Integer.toString(random.nextInt(RAND_BOUND)),
                "Author_" + random.nextInt(RAND_BOUND)
        );
        // Защита от совпадений имен авторов
        List<String> authorNames = authors.stream()
                .map(Author::getFullName)
                .filter(name -> name.contains(author.getFullName()))
                .toList();
        if (! authorNames.isEmpty()) {
            authorRandomGenerator();
        }
        return author;
    }

    public static Book bookRandomGenerator(List<Author> authors, List<Genre> genres) {
        SecureRandom random = getSecureRandom();

        List<Author> randomAuthors = IntStream.range(0, random.nextInt(MAX_BOOK_AUTHORS)+1)
                .mapToObj(i -> authors.get(random.nextInt(authors.size())))
                .collect(Collectors.toList());
        List<Genre> randomGenre = IntStream.range(0, random.nextInt(MAX_BOOK_GENRES)+1)
                .mapToObj(i -> genres.get(random.nextInt(genres.size())))
                .collect(Collectors.toList());
        Book book = new Book("Title_" + random.nextInt(RAND_BOUND),
                randomAuthors,
                randomGenre
        );
        // защита от одинаковых названий книг
        List<String> bookTitles = books.stream()
                .map(Book::getTitle)
                .filter(title -> title.contains(book.getTitle()))
                .toList();
        if (! bookTitles.isEmpty()) {
            bookRandomGenerator(randomAuthors, randomGenre);
        }
        return book;
    }

    public static Comment commentRandomGenerator(Book book) {
        SecureRandom random = getSecureRandom();
        Comment comment = new Comment(book,
                IntStream.range(0, random.nextInt(MAX_COMMENTS_NOTES)+1).boxed()
                        .map(id -> "Title_" + random.nextInt(RAND_BOUND))
                        .collect(Collectors.toList())
        );
        // protection against accidental coincidence
        List<String> genreNames = comments.stream()
                .map(Comment::getTitle)
                .filter(title -> title.contains(comment.getTitle()))
                .toList();
        if (! genreNames.isEmpty()) {
            commentRandomGenerator(book);
        }
        return comment;
    }

    public static Genre genreRandomGenerator() {
        SecureRandom random = getSecureRandom();

        Genre genre = new Genre(Integer.toString(random.nextInt(RAND_BOUND)),
                "Genre_" + random.nextInt(RAND_BOUND)
        );
        // защита от совпадения названий жанров
        List<String> genreNames = genres.stream()
                .map(Genre::getName)
                .filter(name -> name.contains(genre.getName()))
                .toList();
        if (! genreNames.isEmpty()) {
            genreRandomGenerator();
        }
        return genre;
    }

    public static SecureRandom getSecureRandom(){
        SecureRandom random = new SecureRandom();
        byte[] bytes = random.generateSeed(10);
        random.nextBytes(bytes);
        return random;
    }
}
