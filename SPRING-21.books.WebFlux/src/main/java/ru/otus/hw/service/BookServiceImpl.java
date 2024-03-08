package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.mapper.GenreMapper;

import ru.otus.hw.model.Book;
import ru.otus.hw.model.BookGenre;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.repository.BookGenreRepository;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.GenreRepository;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookGenreRepository bookGenreRepository;

    @Override
    public Flux<BookDto> findAll() {
        return bookRepository.findAllBooks();
    }

    @Override
    public Mono<BookDto> findById(Long id) {
        return bookRepository.findByBookId(id)
                .doOnError(throwable -> Mono.error(
                        new EntityNotFoundException("Book with id %d not found".formatted(id))))
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException("Book with id %d not found".formatted(id))));
    }

    @Override
    public Mono<BookDto> insert(BookDto bookDto) {
        bookDto.setId(null);
        return this.save(bookDto);
    }

    @Override
    public Mono<BookDto> update(BookDto bookDto) {
        return this.save(bookDto);
    }

    @Override
    public Mono<BookDto> delete(Long id) {
        return bookRepository.findByBookId(id)
                .doOnSuccess(bookDto -> {
                    bookGenreRepository.deleteByBookID(id).subscribe();
                    bookRepository.deleteById(id).subscribe();
                })
                .doOnError(throwable -> Mono.error(
                        new EntityNotFoundException("Bool not found %d not found, stop deletion".formatted(id))))
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException("Bool not found %d not found, stop deletion".formatted(id))));
    }

    private Mono<BookDto> save(BookDto bookDto) {
        Mono<Book> bookMono = this.saveBook(bookDto);
        Mono<AuthorDto> authorMono = this.findBookAuthor(bookDto);
        Mono<List<GenreDto>> genresMono = this.findBookGenres(bookDto);
        return Mono.zip(bookMono, authorMono, genresMono)
                .map((Tuple3<Book, AuthorDto, List<GenreDto>> tuple) -> new BookDto(
                        tuple.getT1().getId(),
                        tuple.getT1().getTitle(),
                        tuple.getT2(),
                        tuple.getT3()))
                .doOnError(throwable -> {
                    throw new EntityNotFoundException("Error save: \n%s\n".formatted(throwable.getMessage()));
                });
    }

    private Mono<Book> saveBook(BookDto bookDto) {
        return bookRepository.save(BookMapper.toBook(bookDto))
                .doOnSuccess(bookSave -> {
                    bookGenreRepository.deleteByBookID(Objects.requireNonNull(bookSave).getId()).subscribe();
                    bookDto.getGenres().forEach(genre ->
                            bookGenreRepository.save(new BookGenre(bookSave.getId(), genre.getId())).subscribe()
                    );
                })
                .doOnError(throwable -> Mono.error(
                        new EntityNotFoundException("Error: Save Book %d".formatted(bookDto.getId()))));
    }

    private Mono<AuthorDto> findBookAuthor(BookDto bookDto) {
        return authorRepository.findById(bookDto.getAuthor().getId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Author with id %d not found"
                        .formatted(bookDto.getAuthor().getId()))))
                .doOnError(throwable -> Mono.error(new EntityNotFoundException("Error: '%s'"
                        .formatted(throwable.getMessage()))))
                .map(AuthorMapper::toDto);
    }

    private Mono<List<GenreDto>> findBookGenres(BookDto bookDto) {
        List<Long> genresId = bookDto.getGenres().stream().map(GenreDto::getId).toList();
        return genreRepository.findGenresByIdIn(genresId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Genres with ids [%s] not found"
                        .formatted(genresId))))
                .doOnError(throwable -> Mono.error(new EntityNotFoundException("Error: '%s'"
                                .formatted(throwable.getMessage()))))
                .map(GenreMapper::toDto)
                .collectList();
    }

}
