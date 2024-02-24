package ru.otus.hw.repository.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.model.postgres.Author;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findByIdIn(List<Long> id);

}
