package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Genre> findGenreById(long id) {
        Optional<Genre> genre = genreRepository.findById(id);
        if (genre.isEmpty()) {
            throw new EntityNotFoundException("Genres with ids '%d' not found".formatted(id));
        }
        return genre;
    }

    @Transactional
    @Override
    public Genre insert(Genre genre) {
        return this.save(genre);
    }

    @Transactional
    @Override
    public Genre update(Genre genre) {
        Optional<Genre> genreBefore = genreRepository.findById(genre.getId());
        if (genreBefore.isEmpty()) {
            throw new EntityNotFoundException("ERROR: genre '%d' not found".formatted(genre.getId()));
        }
        return this.save(genre);
    }

    @Transactional
    @Override
    public Genre delete(long id) {
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
        return genreBefore;
    }

    private Genre save(Genre genre) {
        return genreRepository.save(genre);
    }
}
