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
import ru.otus.hw.repository.BookRepository;

import java.util.Comparator;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    @Override
    public Flux<AuthorDto> findAll() {
        return authorRepository.findAll()
                .sort(Comparator.comparing(Author::getId))
                .map(AuthorMapper::toDto);

    }

    @Override
    public Mono<AuthorDto> findById(Long id) {
        return authorRepository.findById(id)
                .map(AuthorMapper::toDto)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Author with id '%d' not found".formatted(id))));
    }

    @Override
    public Mono<AuthorDto> insert(AuthorDto authorDto) {
        return authorRepository.save(new Author(authorDto.getFullName()))
                .map(AuthorMapper::toDto)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Author with id '%d' not inserted"
                        .formatted(authorDto.getId()))));
    }

    @Override
    public Mono<AuthorDto> update(AuthorDto authorDto) {
        return authorRepository.findById(authorDto.getId())
                .flatMap(a -> authorRepository.save(AuthorMapper.toAuthor(authorDto)))
                .map(AuthorMapper::toDto)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Author with id '%d' not found, stop update"
                        .formatted(authorDto.getId()))));
    }

    @Override
    public Mono<AuthorDto> delete(Long id) {
        bookRepository.findByAuthorId(id)
                .flatMap(book -> Mono.error(
                        new EntityNotFoundException("The Book for the Author '%d' exists, stop deleting".formatted(id))
                ))
                .blockFirst();

        var authorBefore = authorRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException("ERROR: Author '%d' not found, stop deleting".formatted(id))))
                .block();
        authorRepository.deleteById(id).block();
        authorRepository.findById(id)
                .doOnSuccess(author -> Mono.error(new EntityNotFoundException("ERROR: Author '%d' found after deleting"
                        .formatted(id))))
                .block();
        return Mono.just(AuthorMapper.toDto(Objects.requireNonNull(authorBefore)));
    }

}
