package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.model.Book;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.security.acl.PermissionService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final PermissionService permissionService;

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(BookMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<BookDto> findById(long id) {
        Optional<Book> bookOpt = bookRepository.findAllById(id);
        if (bookOpt.isEmpty()) {
            throw new EntityNotFoundException("Book with id '%s' not found".formatted(id));
        }
        return Optional.of(BookMapper.toDto(bookOpt.get()));
    }


    @PreAuthorize("hasPermission(#book, 'CREATE')")
    @Transactional
    @Override
    public void insert(Book book) {
        save(book);
    }

    @PreAuthorize("hasPermission(#book, 'WRITE')")
    @Transactional
    @Override
    public void update(Book book) {
        save(book);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private void save(Book book) {
        Book saved = bookRepository.save(book);
        permissionService.addPermission(false, saved,
                List.of(BasePermission.READ, BasePermission.WRITE, BasePermission.DELETE));
    }
}
