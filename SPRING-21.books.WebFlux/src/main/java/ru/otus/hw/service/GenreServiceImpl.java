package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.model.Genre;
import ru.otus.hw.repository.BookGenreRepository;
import ru.otus.hw.repository.GenreRepository;

import java.util.Comparator;
import java.util.Objects;


@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final BookGenreRepository bookGenreRepository;

    @Override
    public Flux<GenreDto> findAll() {
        return genreRepository.findAll()
                .sort(Comparator.comparing(Genre::getId))
                .map(GenreMapper::toDto);
    }

    @Override
    public Mono<GenreDto> findById(Long id) {
        return Mono.just(id)
                .flatMap(genreRepository::findById)
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException("Genres with ids '%d' not found".formatted(id))
                )).map(GenreMapper::toDto);
    }

    @Override
    public Mono<GenreDto> insert(GenreDto genreDto) {
        return genreRepository.save(new Genre(genreDto.getName()))
                .map(GenreMapper::toDto);
    }

    @Override
    public Mono<GenreDto> update(GenreDto genreDto) {
        return genreRepository.findById(genreDto.getId())
                .flatMap(a -> genreRepository.save(GenreMapper.toGenre(genreDto)))
                .map(GenreMapper::toDto)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("ERROR: genre '%d' not found"
                        .formatted(genreDto.getId()))));
    }

    @Override
    public Mono<GenreDto> delete(Long id) {
        bookGenreRepository.findByGenreId(id)
                .flatMap(book -> Mono.error(
                        new EntityNotFoundException("The Book use this Genre '%d', stop deleting".formatted(id))
                ))
                .blockFirst();
       var genreBefore = genreRepository.findById(id)
                .map(GenreMapper::toDto)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("ERROR: Genre '%d' not found".formatted(id))))
                .block();

        genreRepository.deleteById(id).subscribe();

        genreRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("ERROR: Genre '%d' not deleted".formatted(id))))
                .block();

        return Mono.just(Objects.requireNonNull(genreBefore));
    }

}
