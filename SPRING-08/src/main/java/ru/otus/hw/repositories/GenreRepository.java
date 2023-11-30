package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.models.Genre;

import java.util.List;

public interface GenreRepository extends MongoRepository<Genre, String>, GenreRepositoryCustom {

    List<Genre> findAll();

    List<Genre> findByNameIn(List<String> names);

    void delete(Genre comment);
}
