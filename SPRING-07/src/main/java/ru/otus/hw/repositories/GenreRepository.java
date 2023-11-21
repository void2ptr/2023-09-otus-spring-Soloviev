package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import ru.otus.hw.models.Genre;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    @NonNull
    @Query("select g from Genre g")
    List<Genre> findAll();

    @Query("SELECT g FROM Genre g WHERE id IN (:ids)")
    List<Genre> findAllByIds(@Param("ids") List<Long> ids);

}
