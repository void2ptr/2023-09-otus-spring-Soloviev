package ru.otus.hw.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.model.Book;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "ControllerBooksFindById", fallbackMethod = "recoverFindById")
    @Override
    public Optional<BookDto> findById(long id) {
        Optional<Book> bookOpt = bookRepository.findAllById(id);
        if (bookOpt.isEmpty()) {
            throw new EntityNotFoundException("Book with id '%s' not found".formatted(id));
        }
        return Optional.of(BookMapper.toDto(bookOpt.get()));
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "ControllerBooksFindAll", fallbackMethod = "recoverFindAll")
    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(BookMapper::toDto)
                .toList();
    }

    @Transactional
    @CircuitBreaker(name = "ControllerBooksInsert", fallbackMethod = "recoverInsert")
    @Override
    public BookDto insert(BookDto bookDto) {
        return save(0, bookDto.getTitle(), bookDto.getAuthor(), bookDto.getGenres());
    }

    @Transactional
    @CircuitBreaker(name = "ControllerBooksUpdate", fallbackMethod = "recoverUpdate")
    @Override
    public BookDto update(BookDto bookDto) {
        return save(bookDto.getId(), bookDto.getTitle(), bookDto.getAuthor(), bookDto.getGenres());
    }

    @Transactional
    @CircuitBreaker(name = "ControllerBooksDelete", fallbackMethod = "recoverDelete")
    @Override
    public BookDto delete(long id) {
        Book bookBefore = bookRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Bool not found %d not found, stop deletion".formatted(id))
        );
        bookRepository.deleteById(id);
        Optional<Book> bookAfter = bookRepository.findById(id);
        if (bookAfter.isPresent()) {
            throw new EntityNotFoundException("Bool found %d do not deleted".formatted(id));
        }
        return BookMapper.toDto(bookBefore);
    }

    private BookDto save(long id, String title, AuthorDto authorDto, List<GenreDto> genresDto) {
        var author = authorRepository.findAuthorById(authorDto.getId());
        if (author.isEmpty()) {
            throw new EntityNotFoundException("Author with id %d not found".formatted(authorDto.getId()));
        }
        var genresIds = genresDto.stream().map(GenreDto::getId).toList();
        var genres = genreRepository.findAllByIdIn(genresIds);
        if (genres.isEmpty()) {
            throw new EntityNotFoundException("Genres with ids [%s] not found".formatted(genresIds));
        }
        var book = new Book(id, title, author.get(), genres);
        return BookMapper.toDto(bookRepository.save(book));
    }

    public Optional<BookDto> recoverFindById(long id, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return findById(id);
    }

    public List<BookDto> recoverFindAll(Exception ex) {
        log.warn(ex.getMessage(), ex);
        return findAll();
    }

    public BookDto recoverInsert(BookDto bookDto, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return insert(bookDto);
    }

    public BookDto recoverUpdate(BookDto bookDto, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return update(bookDto);
    }

    public BookDto recoverDelete(long id, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return delete(id);
    }
}
