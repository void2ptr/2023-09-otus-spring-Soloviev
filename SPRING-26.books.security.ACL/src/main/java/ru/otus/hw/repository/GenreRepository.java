package ru.otus.hw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.otus.hw.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    @PostFilter("hasPermission(filterObject, 'READ')")
    @NonNull
    List<Genre> findAll();

    @PostAuthorize("hasPermission(#id, 'ru.otus.hw.model.Genre', 'READ')")
    @NonNull
    Optional<Genre> findById(long id);

    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Genre> findAllByIdIn(List<Long> ids);

    @PreAuthorize("hasPermission(#genre, 'DELETE')")
    void delete(@NonNull Genre genre);

}
