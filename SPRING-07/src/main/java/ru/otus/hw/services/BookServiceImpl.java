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

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public Optional<Book> findById(long id) {
        return bookRepository.findBookById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> findAll() {
        return bookRepository.findAllBooks();
    }

    @Transactional
    @Override
    public Book insert(String title, long authorId, List<Long> genresIds) {
        return save(0, title, authorId, genresIds);
    }

    @Transactional
    @Override
    public Book update(long id, String title, long authorId, List<Long> genresIds) {
        return save(id, title, authorId, genresIds);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Book save(long id, String title, long authorId, List<Long> genresIds) {
        var author = authorRepository.findAuthorById(authorId);
        if (author.isEmpty()) {
            throw new EntityNotFoundException("Author with id %d not found".formatted(authorId));
        }
        var genres = genreRepository.findAllGenresByIds(genresIds);
        if (genres.isEmpty()) {
            throw new EntityNotFoundException("Genres with ids %s not found".formatted(genresIds));
        }
        var book = new Book(id, title, author.get(), genres);
        return bookRepository.save(book);
    }
}
