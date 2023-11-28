package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends CrudRepository<Author, Long> {

//    @Override
    List<Author> findAll();

//    @Param("id")
    Optional<Author> findById(long id);

}
