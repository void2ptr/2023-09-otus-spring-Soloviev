package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookIdsDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.model.Book;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.GenreRepository;

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
    public void insert(BookIdsDto bookIdsDto) {
        save(0, bookIdsDto.getTitle(), bookIdsDto.getAuthorId(), bookIdsDto.getGenresId());
    }

    @Transactional
    @Override
    public void update(BookIdsDto bookIdsDto) {
        save(bookIdsDto.getId(), bookIdsDto.getTitle(), bookIdsDto.getAuthorId(), bookIdsDto.getGenresId());
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
        var genres = genreRepository.findAllByIdIn(genresIds);
        if (genres.isEmpty()) {
            throw new EntityNotFoundException("Genres with ids [%s] not found".formatted(genresIds));
        }
        var book = new Book(id, title, author.get(), genres);
        bookRepository.save(book);
    }
}
