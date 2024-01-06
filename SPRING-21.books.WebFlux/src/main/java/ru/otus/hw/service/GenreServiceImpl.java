package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.model.Genre;
import ru.otus.hw.repository.GenreRepository;

import java.util.Comparator;


@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

//    private final BookRepository bookRepository;

//    @Transactional(readOnly = true)
    @Override
    public Flux<GenreDto> findAll() {
        return genreRepository.findAll()
                .sort(Comparator.comparing(Genre::getId))
                .map(GenreMapper::toDto);
    }

//    @Transactional(readOnly = true)
    @Override
    public Mono<GenreDto> findGenreById(Long id) {
        return Mono.just(id)
                .flatMap(genreRepository::findGenreById)
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException("Genres with ids '%d' not found".formatted(id))
                )).map(GenreMapper::toDto);
    }

//    @Transactional
    @Override
    public Mono<GenreDto> insert(Genre genre) {
        return genreRepository.save(new Genre(genre.getName()))
                .map(GenreMapper::toDto);
    }

//    @Transactional
    @Override
    public Mono<GenreDto> update(Genre genre) {
        return genreRepository.findGenreById(genre.getId())
                .flatMap(a -> genreRepository.save(genre) )
                .map(GenreMapper::toDto)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("ERROR: genre '%d' not found"
                        .formatted(genre.getId()))));
    }

//    @Transactional
    @Override
    public Mono<GenreDto> delete(Long id) {
//        Flux<Book> books = bookRepository.findAllByGenresId(id);
//        if (!books.isEmpty()) {
//            throw new EntityNotFoundException("The Book use this Genre '%d', stop deleting".formatted(id));
//        }

        Mono<GenreDto> genreBefore = Mono.just(id)
                .flatMap(genreRepository::findGenreById)
                .doOnSuccess(author -> genreRepository.deleteById(author.getId()))
                .map(GenreMapper::toDto)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("ERROR: Genre '%d' not found".formatted(id))));

        Mono.just(id)
                .flatMap(genreRepository::findGenreById)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("ERROR: Genre '%d' not deleted".formatted(id))));

        return genreBefore;
    }

}
