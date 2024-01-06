package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.model.Author;
import ru.otus.hw.repository.AuthorRepository;

import java.util.Comparator;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

//    private final BookRepository bookRepository;

//    @Transactional
    @Override
    public Flux<AuthorDto> findAll() {
        return authorRepository.findAll()
                .sort(Comparator.comparing(Author::getId))
                .map(AuthorMapper::toDto);

    }

//    @Transactional(readOnly = true)
    @Override
    public Mono<AuthorDto> findAuthorById(Long id) {
        return Mono.just(id)
                .flatMap(authorRepository::findAuthorById)
                .map(AuthorMapper::toDto)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Author with id '%d' not found".formatted(id))));
    }

//    @Transactional
    @Override
    public Mono<AuthorDto> insert(Author author) {
        return authorRepository.save(new Author(author.getFullName()))
                .map(AuthorMapper::toDto);
    }

//    @Transactional
    @Override
    public Mono<AuthorDto> update(Author author) {
        return authorRepository.findAuthorById(author.getId())
                .flatMap(a -> authorRepository.save(author) )
                .map(AuthorMapper::toDto)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Author with id '%d' not found, stop update"
                        .formatted(author.getId()))));
    }

//    @Transactional
    @Override
    public Mono<AuthorDto> delete(Long id) {
//        Flux<Book> books = bookRepository.findAllBooksByAuthorId(id);
//        if (!books.isEmpty()) {
//            throw new EntityNotFoundException("The Book for the Author '%d' exists, stop deleting"
//                    .formatted(id));
//        }

        Mono<AuthorDto> authorBefore = authorRepository.findAuthorById(id)
                .doOnSuccess(author ->
                        authorRepository.deleteById(id))
                .map(author -> AuthorMapper.toDto(author))
                .doOnError(author -> Mono.error(
                        new EntityNotFoundException("ERROR: Author '%d' not found, stop deleting".formatted(id))));

        authorRepository.findAuthorById(id)
                .doOnError(author -> Mono.error(new EntityNotFoundException("ERROR: Author '%d' found after deleting"
                        .formatted(id))));

        return authorBefore;
    }

}
