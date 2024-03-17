package ru.otus.hw.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.repository.BookRepository;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Slf4j
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;


    @Transactional(readOnly = true)
    @CircuitBreaker(name = "ServerAuthorFindAll", fallbackMethod = "recoverFindAll")
    @Override
    public List<AuthorDto> findAll() {
        return authorRepository.findAll()
                .stream()
                .map(AuthorMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "ServerAuthorFindById", fallbackMethod = "recoverFindById")
    @Override
    public AuthorDto findAuthorById(long id) {
        return AuthorMapper.toDto(authorRepository.findAuthorById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author with id '%d' not found".formatted(id))));
    }

    @Transactional
    @CircuitBreaker(name = "ServerAuthorInsert", fallbackMethod = "recoverInsert")
    @Override
    public AuthorDto insert(AuthorDto authorDto) {
        return this.save(new AuthorDto(0, authorDto.getFullName()));
    }

    @Transactional
    @CircuitBreaker(name = "ServerAuthorsUpdate", fallbackMethod = "recoverUpdate")
    @Override
    public AuthorDto update(AuthorDto authorDto) {
        authorRepository.findAuthorById(authorDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Author with id '%d' not found, stop update"
                        .formatted(authorDto.getId())));

        return this.save(authorDto);
    }

    @Transactional
    @CircuitBreaker(name = "ServerAuthorsDelete", fallbackMethod = "recoverDelete")
    @Override
    public AuthorDto delete(long id) {
        List<Book> books = bookRepository.findAllBooksByAuthorId(id);
        if (!books.isEmpty()) {
            throw new EntityNotFoundException("The Book for the Author '%d' exists, stop deleting"
                    .formatted(id));
        }
        var authorBefore = authorRepository.findAuthorById(id).orElseThrow(() ->
                        new EntityNotFoundException("ERROR: Author '%d' not found, stop deleting"
                                .formatted(id)));
        authorRepository.delete(authorBefore);

        Optional<Author> authorAfter = authorRepository.findAuthorById(id);
        if (authorAfter.isPresent()) {
            throw new EntityNotFoundException("ERROR: Author '%d' found after deleting"
                    .formatted(id));
        }
        return AuthorMapper.toDto(authorBefore);
    }

    private AuthorDto save(AuthorDto authorDto) {
        return AuthorMapper.toDto(authorRepository.save(AuthorMapper.toAuthor(authorDto)));
    }

    public List<AuthorDto> recoverFindAll(Exception ex) {
        log.warn(ex.getMessage(), ex);

        return findAll();
    }

    public AuthorDto recoverFindById(Long id, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return findAuthorById(id);
    }

    public AuthorDto recoverInsert(AuthorDto authorDto, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return insert(authorDto);
    }

    public AuthorDto recoverUpdate(AuthorDto authorDto, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return update(authorDto);
    }

    public AuthorDto recoverDelete(long id, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return delete(id);
    }

}
