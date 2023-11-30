package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

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
            throw new EntityNotFoundException("Book %s present".formatted(title));
        }
        return save(title, authorNames, genresIds);
    }

    @Transactional
    @Override
    public Book update(String title, List<String> authorNames, List<String> genresIds) {
        Optional<Book> book = bookRepository.findByTitleIs(title);
        if (book.isEmpty()) {
            throw new EntityNotFoundException("Book with id %s not found".formatted(title));
        }
        return save(title, authorNames, genresIds);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    private Book save(String title, List<String> authorNames, List<String> genresIds) {
        var authors = authorRepository.findByFullNameIn(authorNames);
        if (isEmpty(authors)) {
            throw new EntityNotFoundException("Authors with ids %s not found".formatted(authors));
        }
        var genres = genreRepository.findByNameIn(genresIds);
        if (isEmpty(genres)) {
            throw new EntityNotFoundException("Genres with ids %s not found".formatted(genresIds));
        }
        var book = new Book(title, authors, genres);
        return bookRepository.save(book);
    }
}
