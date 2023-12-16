package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll()
                .stream()
                .map(GenreMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<GenreDto> findGenreById(long id) {
        Optional<Genre> genre = genreRepository.findById(id);
        if (genre.isEmpty()) {
            throw new EntityNotFoundException("Genres with ids '%d' not found".formatted(id));
        }
        return Optional.of(GenreMapper.toDto(genre.get()));
    }

    @Transactional
    @Override
    public void insert(GenreDto genreDto) {
        this.save(genreDto);
    }

    @Transactional
    @Override
    public void update(GenreDto genreDto) {
        Optional<Genre> genreOptional = genreRepository.findById(genreDto.getId());
        if (genreOptional.isEmpty()) {
            throw new EntityNotFoundException("ERROR: genre '%d' not found".formatted(genreDto.getId()));
        }
        this.save(genreDto);
    }

    @Transactional
    @Override
    public void delete(long genreId) {
        List<Book> books = bookRepository.findAllBooksByGenreId(genreId);
        if (!books.isEmpty()) {
            throw new EntityNotFoundException("The Book for the Genre '%d' exists, stop deleting".formatted(genreId));
        }

        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("ERROR: Genre '%d' not found".formatted(genreId)));
        genreRepository.delete(genre);
    }

    private void save(GenreDto genreDto) {
        genreRepository.save(GenreMapper.toGenre(genreDto));
    }
}
