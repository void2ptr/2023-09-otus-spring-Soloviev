package ru.otus.hw.repository.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.model.postgres.Author;
import ru.otus.hw.model.postgres.Genre;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    List<Genre> findByIdIn(List<Long> id);

}
