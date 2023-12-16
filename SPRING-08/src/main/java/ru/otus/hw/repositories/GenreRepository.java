package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;
import ru.otus.hw.models.Genre;

import java.util.List;

public interface GenreRepository extends MongoRepository<Genre, String> {

    @NonNull
    List<Genre> findAll();

    List<Genre> findByNameIn(List<String> names);

}
