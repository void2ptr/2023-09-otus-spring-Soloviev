package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @NonNull
    @Query("select a from Author a")
    List<Author> findAll();

    @Query("select a from Author a where id = :id")
    Optional<Author> findById(@Param("id") long id);

}
