package ru.otus.hw.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.hw.model.Author;

public interface AuthorRepository extends ReactiveCrudRepository<Author, Long> {

}
