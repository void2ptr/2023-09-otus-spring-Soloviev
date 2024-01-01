package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public List<AuthorDto> findAll() {
        return authorRepository.findAll().stream()
                .map(AuthorMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public AuthorDto findAuthorById(long id) {
        Author author = authorRepository.findAuthorById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author with id '%d' not found".formatted(id)));
        return AuthorMapper.toDto(author);
    }

    @Transactional
    @Override
    public Optional<AuthorDto> insert(AuthorDto authorDto) {
        return this.save(authorDto);
    }

    @Transactional
    @Override
    public Optional<AuthorDto> update(AuthorDto authorDto) {
        authorRepository.findAuthorById(authorDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Author with id '%d' not found, stop update"
                        .formatted(authorDto.getId())));
        return this.save(authorDto);
    }

    @Transactional
    @Override
    public boolean delete(long authorId) {
        List<Book> books = bookRepository.findAllBooksByAuthorId(authorId);
        if (!books.isEmpty()) {
            throw new EntityNotFoundException("The Book for the Author '%d' exists, stop deleting".formatted(authorId));
        }

        var author = authorRepository.findAuthorById(authorId)
                .orElseThrow(() ->
                        new EntityNotFoundException("ERROR: Author '%d' not found, stop deleting".formatted(authorId)));
        authorRepository.delete(author);

        return authorRepository.findAuthorById(authorId).isEmpty();
    }

    private Optional<AuthorDto> save(AuthorDto authorDto) {
        return Optional.of(AuthorMapper.toDto(authorRepository.save(AuthorMapper.toAuthor(authorDto))));
    }
}
