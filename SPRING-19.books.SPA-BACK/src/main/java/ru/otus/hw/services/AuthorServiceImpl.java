package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
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
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Author findAuthorById(long id) {
        return authorRepository.findAuthorById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author with id '%d' not found".formatted(id)));
    }

    @Transactional
    @Override
    public Author insert(Author author) {
        return this.save(new Author(0, author.getFullName()));
    }

    @Transactional
    @Override
    public Author update(Author author) {
        authorRepository.findAuthorById(author.getId())
                .orElseThrow(() -> new EntityNotFoundException("Author with id '%d' not found, stop update"
                        .formatted(author.getId())));

        return this.save(author);
    }

    @Transactional
    @Override
    public Author delete(long id) {
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
        return authorBefore;
    }

    private Author save(Author author) {
        return authorRepository.save(author);
    }
}
