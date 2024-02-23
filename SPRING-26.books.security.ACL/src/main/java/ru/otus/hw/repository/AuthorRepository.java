package ru.otus.hw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.otus.hw.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @PostFilter("hasPermission(filterObject, 'READ')")
    @NonNull
    List<Author> findAll();

    @PostAuthorize("hasPermission(#id, 'ru.otus.hw.model.Author', 'READ')")
    Optional<Author> findAuthorById(long id);

    @PreAuthorize("hasPermission(#author, 'DELETE')")
    void delete(@NonNull Author author);

}
