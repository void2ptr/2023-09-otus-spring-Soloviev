package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends MongoRepository<Author, String> {

//    @Override
    List<Author> findAll();

//    List<Author> findAllById(List<String> ids);

    @Query("{ 'fullName' : { $regex: ?0 } }")
    List<Author> findAuthorsByRegexp(String regexp);

    List<Author> findByFullNameIn(List<String> fullNames);

//    @Param("id")
    Optional<Author> findById(String id);

}
