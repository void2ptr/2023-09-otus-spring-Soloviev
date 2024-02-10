package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.GenreRepository;
import ru.otus.hw.security.acl.PermissionService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final PermissionService permissionService;

    @Transactional(readOnly = true)
    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream()
                .map(GenreMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<GenreDto> findGenreById(long id) {
        Optional<Genre> genre = genreRepository.findGenreById(id);
        if (genre.isEmpty()) {
            throw new EntityNotFoundException("Genres with ids '%d' not found".formatted(id));
        }
        return Optional.of(GenreMapper.toDto(genre.get()));
    }

    @PreAuthorize("hasPermission(#genre, 'CREATE')")
    @Transactional
    @Override
    public void insert(Genre genre) {
        this.save(genre);
    }

    @PreAuthorize("hasPermission(#genre, 'WRITE')")
    @Transactional
    @Override
    public void update(Genre genre) {
        Optional<Genre> genreOptional = genreRepository.findById(genre.getId());
        if (genreOptional.isEmpty()) {
            throw new EntityNotFoundException("ERROR: genre '%d' not found".formatted(genre.getId()));
        }
        this.save(genre);
    }

    @Transactional
    @Override
    public void delete(long genreId) {
        List<Book> books = bookRepository.findByGenresId(genreId);
        if (!books.isEmpty()) {
            throw new EntityNotFoundException("The Book for the Genre '%d' exists, stop deleting".formatted(genreId));
        }

        var genre = genreRepository.findGenreById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("ERROR: Genre '%d' not found".formatted(genreId)));
        genreRepository.delete(genre);
    }

//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    private void save(Genre genre) {

        Genre saved = genreRepository.save(genre);

        permissionService.addPermission(false, Genre.class, saved.getId(),
                List.of(BasePermission.READ, BasePermission.WRITE, BasePermission.DELETE));
    }
}
