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
    public void insert(AuthorDto authorDto) {
        this.save(authorDto);
    }

    @Transactional
    @Override
    public void update(AuthorDto authorDto) {
        this.save(authorDto);
    }

    @Transactional
    @Override
    public void delete(long authorId) {
        List<Book> books = bookRepository.findAllBooksByAuthorId(authorId);
        if (!books.isEmpty()) {
            throw new EntityNotFoundException("The Book for the Author '%d' exists, stop deleting".formatted(authorId));
        }

        var author = authorRepository.findAuthorById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("ERROR: Author '%d' not found".formatted(authorId)));
        authorRepository.delete(author);
    }

    private void save(AuthorDto authorDto) {
        var authorById = authorRepository.findAuthorById(authorDto.getId());
        Author author;
        if (authorById.isEmpty()) {
            author = new Author(0, authorDto.getFullName());
        } else {
            author = AuthorMapper.toAuthor(authorDto);
        }
        authorRepository.save(author);
    }
}
