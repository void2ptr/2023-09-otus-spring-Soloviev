package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.mapper.GenreMapper;

import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.BookGenre;
import ru.otus.hw.model.Genre;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.repository.BookGenreRepository;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.GenreRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookGenreRepository bookGenreRepository;

    @Override
    public Flux<BookDto> findAll() {
        return mergeBooksInfo(
                bookRepository.findAll().collectMap(Book::getId, book -> book).block(),
                authorRepository.findAll().collectMap(Author::getId, author -> author).block(),
                genreRepository.findAll().collectMap(Genre::getId, genre -> genre).block(),
                Objects.requireNonNull(bookGenreRepository.findAll().collectList().block())
        );
    }

    @Override
    public Mono<BookDto> findById(BookDto bookDto) {
        return mergeBooksInfo(
                bookRepository.findById(bookDto.getId()).map(book -> Map.of(book.getId(), book)).block(),
                authorRepository.findById(bookDto.getAuthor().getId())
                        .map(author -> Map.of(author.getId(), author)).block(),
                genreRepository.findGenresByIdIn(
                        bookDto.getGenres().stream().map(GenreDto::getId).collect(Collectors.toList())
                        ).collectMap(Genre::getId, genre -> genre).block(),
                Objects.requireNonNull(bookGenreRepository.findByBookIdIn(bookDto.getId()).collectList().block())
        ).next();
    }

    private Flux<BookDto> mergeBooksInfo(Map<Long, Book> booksMap,
                                         Map<Long, Author> authorsMap,
                                         Map<Long, Genre> genresMap,
                                         List<BookGenre> relations
    ) {
        Map<Long, BookDto> booksDto = new HashMap<>();
        relations.forEach(relation -> {
            Long bookId = relation.getBookId();
            BookDto bookDto = booksDto.get(bookId);
            GenreDto genre = GenreMapper.toDto(genresMap.get(relation.getGenreId()));
            if (bookDto == null) {
                ru.otus.hw.model.Book book = booksMap.get(relation.getBookId());
                AuthorDto authorDto = AuthorMapper.toDto(authorsMap.get(book.getAuthorId()));
                booksDto.put(bookId, new BookDto(book.getId(), book.getTitle(), authorDto,
                        new ArrayList<>(List.of(genre))));
            } else {
                bookDto.getGenres().add(genre);
            }
        });
        List<BookDto> list = new ArrayList<>();
        booksDto.forEach((k, v) -> list.add(v));
        return Flux.fromIterable(list);
    }

    @Override
    public Mono<BookDto> insert(BookDto bookDto) {
        return this.save(new Book(
                bookDto.getTitle(), bookDto.getAuthor().getId()),
                bookDto.getGenres().stream().map(GenreMapper::toGenre).toList()
        );
    }

    @Override
    public Mono<BookDto> update(BookDto bookDto) {
        return this.save(new Book(
                bookDto.getId(), bookDto.getTitle(), bookDto.getAuthor().getId()),
                bookDto.getGenres().stream().map(GenreMapper::toGenre).toList()
        );
    }

    @Override
    public Mono<BookDto> delete(Long id) {
        var bookBefore = bookRepository.findById(id)
                .map(BookMapper::toDto)
                .doOnError(book -> Mono.error(
                        new EntityNotFoundException("Bool not found %d not found, stop deletion".formatted(id))))
                .block();

        bookRepository.deleteById(id).block();
        bookGenreRepository.deleteByBookID(id).block();

        bookRepository.findById(id)
                .doOnSuccess(book -> Mono.error(
                        new EntityNotFoundException("Bool not found %d not found, stop deletion".formatted(id))))
                .block();

        return Mono.just(Objects.requireNonNull(bookBefore));
    }

    private Mono<BookDto> save(Book book, List<Genre> genres) {
        List<Long> genresId = genres.stream()
                .map(Genre::getId)
                .toList();
        AuthorDto authorDto = authorRepository.findById(book.getAuthorId())
                .map(AuthorMapper::toDto)
                .doOnError(a -> Mono.error(
                        new EntityNotFoundException("Author with id %d not found".formatted(book.getAuthorId()))))
                .block();
        List<GenreDto> genresDto = genreRepository.findGenresByIdIn(genresId)
                .map(GenreMapper::toDto)
                .doOnError(g -> Mono.error(
                        new EntityNotFoundException("Genres with ids [%s] not found".formatted(genresId))))
                .collectList()
                .block();

        Book bookSave = bookRepository.save(book).block();
        bookGenreRepository.deleteByBookID(Objects.requireNonNull(bookSave).getId()).block();
        genres.forEach(genre -> bookGenreRepository.save(new BookGenre(bookSave.getId(), genre.getId())).block());

        return Mono.just(new BookDto(bookSave.getId(), bookSave.getTitle(), authorDto, genresDto));
    }
}
