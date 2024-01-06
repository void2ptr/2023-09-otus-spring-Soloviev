package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.BookModel;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

//    @Transactional(readOnly = true)
    @Override
    public Flux<BookDto> findAll() {
        return bookRepository.findAll()
                .map(BookMapper::toDto);
    }

//    @Transactional(readOnly = true)
    @Override
    public Mono<BookDto> findById(Long id) {
        return bookRepository.findBookById(id)
                .map(BookMapper::toDto)
                .doOnError(book -> Mono.error(
                        new EntityNotFoundException("Book with id '%s' not found".formatted(id))));
    }

//    @Transactional
    @Override
    public Mono<BookDto> insert(BookDto bookDto) {
        BookModel bookModel = BookMapper.toBook(bookDto);
        return save( new BookModel(bookDto.getTitle(), bookModel.getAuthor(), bookModel.getGenres()));
    }

//    @Transactional
    @Override
    public Mono<BookDto> update(BookDto bookDto) {
        return save(BookMapper.toBook(bookDto));
    }

//    @Transactional
    @Override
    public Mono<BookDto> delete(Long id) {
        var bookBefore = bookRepository.findById(id)
                .map(BookMapper::toDto)
                .doOnError(book -> Mono.error(
                        new EntityNotFoundException("Bool not found %d not found, stop deletion".formatted(id))));

        bookRepository.deleteById(id);

        bookRepository.findById(id)
                .doOnSuccess(book -> Mono.error(
                        new EntityNotFoundException("Bool not found %d not found, stop deletion".formatted(id))));

        return bookBefore;
    }

    private Mono<BookDto> save(BookModel bookModel) {
        Mono<Author> authorMono = authorRepository.findAuthorById(bookModel.getAuthor().getId())
                .doOnError(author -> Mono.error(
                        new EntityNotFoundException("Author with id %d not found"
                                .formatted(bookModel.getAuthor().getId()))));

        List<Long> genresIds = bookModel.getGenres().stream()
                .map(genre -> genre.getId())
                .toList();

        var genresMono = genreRepository.findGenresByIdIn(genresIds)
                .collectList()
                .doOnError(author -> Mono.error(
                        new EntityNotFoundException("Genres with ids [%s] not found".formatted(genresIds))));

        return bookRepository.save(
                new BookModel(bookModel.getId(), bookModel.getTitle(), authorMono.block(), genresMono.block()))
                .map(BookMapper::toDto);
    }
}
