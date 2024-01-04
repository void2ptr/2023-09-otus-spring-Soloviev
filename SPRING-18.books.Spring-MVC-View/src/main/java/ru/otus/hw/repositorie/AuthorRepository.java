package ru.otus.hw.repositorie;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.model.Author;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findAuthorById(long id);

}
