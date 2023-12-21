package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
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
    public Optional<BookDto> findById(long id) {
        Optional<Book> bookOpt = bookRepository.findAllById(id);
        if (bookOpt.isEmpty()) {
            throw new EntityNotFoundException("Book with id '%s' not found".formatted(id));
        }
        return Optional.of(BookMapper.toDto(bookOpt.get()));
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(BookMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<AuthorDto> findAllAuthorsNotInBook(long bookId) {
        List<Author> authors = authorRepository.findAll();
        Optional<Book> bookById = bookRepository.findAllById(bookId);
        bookById.ifPresent(book -> authors.removeAll(List.of(book.getAuthor())));

        return authors.stream()
                .map(AuthorMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<GenreDto> findAllGenresNotInBook(long bookId) {
        List<Genre> genres = genreRepository.findAll();
        Optional<Book> bookById = bookRepository.findAllById(bookId);
        bookById.ifPresent(book -> genres.removeAll(book.getGenres()));

        return genres.stream()
                .map(GenreMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public void insert(String title, long authorId, List<Long> genresIds) {
        save(0, title, authorId, genresIds);
    }

    @Transactional
    @Override
    public void update(long id, String title, long authorId, List<Long> genresIds) {
        save(id, title, authorId, genresIds);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private void save(long id, String title, long authorId, List<Long> genresIds) {
        var author = authorRepository.findAuthorById(authorId);
        if (author.isEmpty()) {
            throw new EntityNotFoundException("Author with id %d not found".formatted(authorId));
        }
        var genres = genreRepository.findAllGenresByIds(genresIds);
        if (genres.isEmpty()) {
            throw new EntityNotFoundException("Genres with ids %s not found".formatted(genresIds));
        }
        var book = new Book(id, title, author.get(), genres);
        bookRepository.save(book);
    }
}
