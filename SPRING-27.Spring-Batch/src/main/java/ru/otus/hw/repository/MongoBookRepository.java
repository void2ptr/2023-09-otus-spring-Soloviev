package ru.otus.hw.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import ru.otus.hw.model.mongo.MongoBook;

import java.util.Optional;

public interface MongoBookRepository extends MongoRepository<MongoBook, String> {

    @Query("{ 'externalLink' : ?0 }")
    Optional<MongoBook> findByExternalLinkId(String externalLink);

}
