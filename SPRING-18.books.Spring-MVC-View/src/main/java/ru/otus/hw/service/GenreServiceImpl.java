package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;
import ru.otus.hw.repositorie.BookRepository;
import ru.otus.hw.repositorie.GenreRepository;

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

    @Override
    public List<GenreDto> findGenresNotInBook(long bookId) {
        List<Genre> genres = genreRepository.findAll();
        Optional<Book> bookById = bookRepository.findAllById(bookId);
        bookById.ifPresent(book -> genres.removeAll(book.getGenres()));

        return genres.stream()
                .map(GenreMapper::toDto)
                .toList();
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
        List<Book> books = bookRepository.findAllByGenresId(genreId);
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
