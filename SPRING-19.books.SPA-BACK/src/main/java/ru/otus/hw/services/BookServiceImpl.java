package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.BookMapper;
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

    @Transactional
    @Override
    public BookDto insert(BookDto bookDto) {
        return save(0, bookDto.getTitle(), bookDto.getAuthor(), bookDto.getGenres());
    }

    @Transactional
    @Override
    public BookDto update(BookDto bookDto) {
        return save(bookDto.getId(), bookDto.getTitle(), bookDto.getAuthor(), bookDto.getGenres());
    }

    @Transactional
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
}
