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
    public Mono<BookDto> findById(BookDto bookDto) {

        Mono<Book> bookMono = bookRepository.findById(bookDto.getId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Book with id %d not found"
                                .formatted(bookDto.getAuthor().getId()))));
        Mono<AuthorDto> authorMono = this.findBookAuthor(bookDto);
        Mono<List<GenreDto>> genresMono = this.findBookGenres(bookDto);
        return Flux.zip(bookMono, authorMono, genresMono)
                .map((Tuple3<Book, AuthorDto, List<GenreDto>> tuple) -> new BookDto(
                        tuple.getT1().getId(),
                        tuple.getT1().getTitle(),
                        tuple.getT2(),
                        tuple.getT3()))
                .doOnError(e -> {
                    throw new EntityNotFoundException("Error book not found: \n%s\n".formatted(e.getMessage())); })
                .next();
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
        return bookRepository.findById(id)
                .doOnError(book -> Mono.error(
                        new EntityNotFoundException("Bool not found %d not found, stop deletion".formatted(id))))
                .doOnSuccess(book -> bookRepository.deleteById(id))
                .doOnSuccess(book -> bookGenreRepository.deleteByBookID(id))
                .doOnSuccess(book -> bookRepository.findById(id)
                        .doOnSuccess(b -> Mono.error(
                                new EntityNotFoundException("Bool not found %d not found, error deletion"
                                        .formatted(id)))))
                .map(BookMapper::toDto);
    }

    private Mono<BookDto> save(BookDto bookDto) {
        Mono<Book> bookMono = this.saveBook(bookDto);
        Mono<AuthorDto> authorMono = this.findBookAuthor(bookDto);
        Mono<List<GenreDto>> genresMono = this.findBookGenres(bookDto);
        return Flux.zip(bookMono, authorMono, genresMono)
                .map((Tuple3<Book, AuthorDto, List<GenreDto>> tuple) -> new BookDto(
                        tuple.getT1().getId(),
                        tuple.getT1().getTitle(),
                        tuple.getT2(),
                        tuple.getT3()))
                .doOnError(e -> {
                    throw new EntityNotFoundException("Error save: \n%s\n".formatted(e.getMessage())); })
                .next();
    }

    private Mono<Book> saveBook(BookDto bookDto) {
        return bookRepository.save(BookMapper.toBook(bookDto))
                .doOnSuccess(bookSave -> bookGenreRepository.deleteByBookID(Objects.requireNonNull(bookSave).getId()))
                .doOnSuccess(bookSave -> bookDto.getGenres().forEach(genre ->
                        bookGenreRepository.save(new BookGenre(bookSave.getId(), genre.getId()))));
    }

    private Mono<AuthorDto> findBookAuthor(BookDto bookDto) {
        return authorRepository.findById(bookDto.getAuthor().getId())
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException("Author with id %d not found"
                                .formatted(bookDto.getAuthor().getId()))))
                .map(AuthorMapper::toDto);
    }

    private Mono<List<GenreDto>> findBookGenres(BookDto bookDto) {
        List<Long> genresId = bookDto.getGenres().stream().map(GenreDto::getId).toList();
        return genreRepository.findGenresByIdIn(genresId)
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException("Genres with ids [%s] not found".formatted(genresId))))
                .map(GenreMapper::toDto).collectList();
    }

}
