package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.exceptions.EntityTooManyException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public Optional<Book> findById(String id) {
        return bookRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional
    @Override
    public Book insert(String title, List<String> authorNames, List<String> genresIds) {
        Optional<Book> book = bookRepository.findByTitleIs(title);
        if (book.isPresent()) {
            throw new EntityTooManyException("Error: A book with the title '%s' was found.".formatted(title));
        }
        return save(title, authorNames, genresIds);
    }

    @Transactional
    @Override
    public Book update(String title, List<String> authorNames, List<String> genresIds) {
        return save(title, authorNames, genresIds);
    }

    @Transactional
    @Override
    public void deleteByTitle(String title) {
        Book book;
        List<Book> books = bookRepository.findAllByTitleIs(title);
        if (books.isEmpty()) {
            throw new EntityNotFoundException("Error: Book with the title '%s' not found.".formatted(title));
        } else if (books.size() > 1) {
            throw new EntityTooManyException("Error: Books with the title '%s' were found.".formatted(title));
        }
        book = books.get(0);

        bookRepository.delete(book);
    }

    private Book save(String title, List<String> authorNames, List<String> genresIds) {
        var authors = authorRepository.findByFullNameIn(authorNames);
        if (authors.isEmpty()) {
            throw new EntityNotFoundException("Error: Authors [%s] not found".formatted(authors));
        }
        var genres = genreRepository.findByNameIn(genresIds);
        if (genres.isEmpty()) {
            throw new EntityNotFoundException("Error: Genres [%s] not found".formatted(genresIds));
        }
        Book book;
        List<Book> books = bookRepository.findAllByTitleIs(title);
        if (books.size() > 1) {
            throw new EntityTooManyException("Error: Books with the title '%s' were found.".formatted(title));
        } else if (books.isEmpty()) {
            book = new Book(title, authors, genres);
        } else {
            book = books.get(0);
            book.setAuthors(authors);
            book.setGenres(genres);
        }

        return bookRepository.save(book);
    }
}
