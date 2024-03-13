package ru.otus.hw.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "ControllerGenresFindAll", fallbackMethod = "recoverFindAll")
    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll()
                .stream()
                .map(GenreMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "ControllerGenresFindById", fallbackMethod = "recoverFindById")
    @Override
    public Optional<GenreDto> findGenreById(long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genres with ids '%d' not found".formatted(id)));

        return Optional.of(GenreMapper.toDto(genre));
    }

    @Transactional
    @CircuitBreaker(name = "ControllerGenresInsert", fallbackMethod = "recoverInsert")
    @Override
    public GenreDto insert(GenreDto genre) {
        return this.save(genre);
    }

    @Transactional
    @CircuitBreaker(name = "ControllerGenresUpdate", fallbackMethod = "recoverUpdate")
    @Override
    public GenreDto update(GenreDto genre) {
        Optional<Genre> genreBefore = genreRepository.findById(genre.getId());
        if (genreBefore.isEmpty()) {
            throw new EntityNotFoundException("ERROR: genre '%d' not found".formatted(genre.getId()));
        }
        return this.save(genre);
    }

    @Transactional
    @CircuitBreaker(name = "ControllerGenresDelete", fallbackMethod = "recoverDelete")
    @Override
    public GenreDto delete(long id) {
        List<Book> books = bookRepository.findAllByGenresId(id);
        if (!books.isEmpty()) {
            throw new EntityNotFoundException("The Book use this Genre '%d', stop deleting".formatted(id));
        }
        var genreBefore = genreRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("ERROR: Genre '%d' not found".formatted(id)));
        genreRepository.delete(genreBefore);
        var genreAfter = genreRepository.findById(id);
        if (genreAfter.isPresent()) {
            throw new EntityNotFoundException("ERROR: Genre '%d' not deleted".formatted(id));
        }
        return GenreMapper.toDto(genreBefore);
    }

    private List<GenreDto> recoverFindAll(Exception ex) {
        log.warn(ex.getMessage(), ex);
        return findAll();
    }

    private Optional<GenreDto> recoverFindById(long id, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return findGenreById(id);
    }

    private GenreDto save(GenreDto genreDto) {
        return GenreMapper.toDto(genreRepository.save(GenreMapper.toGenre(genreDto)));
    }

    private GenreDto recoverInsert(GenreDto genreDto, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return insert(genreDto);
    }

    private GenreDto recoverUpdate(GenreDto genreDto, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return update(genreDto);
    }

    private GenreDto recoverDelete(long id, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return delete(id);
    }
}
