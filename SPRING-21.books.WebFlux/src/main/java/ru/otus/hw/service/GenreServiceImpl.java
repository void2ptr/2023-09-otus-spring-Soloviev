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

    @Override
    public Flux<GenreDto> findAll() {
        return genreRepository.findAll()
                .sort(Comparator.comparing(Genre::getId))
                .map(GenreMapper::toDto);
    }

    @Override
    public Mono<GenreDto> findById(Long id) {
        return genreRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException("Genres with ids '%d' not found".formatted(id))))
                .map(GenreMapper::toDto);
    }

    @Override
    public Mono<GenreDto> insert(GenreDto genreDto) {
        return genreRepository.save(new Genre(genreDto.getName()))
                .map(GenreMapper::toDto);
    }

    @Override
    public Mono<GenreDto> update(GenreDto genreDto) {
        return genreRepository.findById(genreDto.getId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("ERROR: genre '%d' not found"
                        .formatted(genreDto.getId()))))
                .flatMap(genre -> genreRepository.save(GenreMapper.toGenre(genreDto)))
                .map(GenreMapper::toDto);
    }

    @Override
    public Mono<GenreDto> delete(Long id) {
        return genreRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("ERROR: Genre '%d' not found".formatted(id))))
                .doOnSuccess(genre -> genreRepository.deleteById(id))
                .doOnSuccess(genre -> genreRepository.findById(id)
                        .switchIfEmpty(Mono.error(new EntityNotFoundException("ERROR: Genre '%d' not deleted"
                                .formatted(id)))))
                .map(GenreMapper::toDto);
    }

}
