package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;
import ru.otus.hw.models.Author;

import java.util.List;

public interface AuthorRepository extends MongoRepository<Author, String> {

    @NonNull
    List<Author> findAll();

    @NonNull
    List<Author> findByFullNameIn(List<String> fullNames);

}
