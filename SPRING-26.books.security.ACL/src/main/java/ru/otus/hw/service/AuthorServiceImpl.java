package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.model.Author;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.security.acl.PermissionService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    private final PermissionService permissionService;

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

    @PreAuthorize("hasPermission(#author, 'CREATE')")
    @Transactional
    @Override
    public Optional<Author> insert(Author author) {
        return this.save(author);
    }

    @PreAuthorize("hasPermission(#author, 'WRITE')")
    @Transactional
    @Override
    public Optional<Author> update(Author author) {
        if (!authorRepository.existsById(author.getId())) {
            throw new EntityNotFoundException("Author with id '%d' not found, stop update".formatted(author.getId()));
        }

        return this.save(author);
    }

    @Transactional
    @Override
    public boolean delete(long authorId) {
        if (bookRepository.existsById(authorId)) {
            throw new EntityNotFoundException("The Book for the Author '%d' exists, stop deleting".formatted(authorId));
        }

        var author = authorRepository.findAuthorById(authorId)
                .orElseThrow(() ->
                        new EntityNotFoundException("ERROR: Author '%d' not found, stop deleting".formatted(authorId)));
        authorRepository.delete(author);

        return authorRepository.findAuthorById(authorId).isEmpty();
    }

    private Optional<Author> save(Author author) {
        Author saved = authorRepository.save(author);
        permissionService.addPermission(false, saved,
                List.of(BasePermission.READ, BasePermission.WRITE, BasePermission.DELETE));
        return Optional.of(saved);
    }
}
