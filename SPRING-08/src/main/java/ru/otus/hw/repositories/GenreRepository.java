package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.models.Genre;

import java.util.List;

public interface GenreRepository extends CrudRepository<Genre, Long> {

//    @Override
//    @Query("select g from Genre g")
    List<Genre> findAll();

//    @Query("SELECT g FROM Genre g WHERE id IN (:ids)")
    List<Genre> findAllById(List<Long> ids);

    void delete(Genre comment);
}
