package ru.otus.hw.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import ru.otus.hw.model.mongo.MongoAuthor;

import java.util.Optional;

public interface MongoAuthorRepository extends MongoRepository<MongoAuthor, String>  {

    @Query("{ 'externalLink' : ?0 }")
    Optional<MongoAuthor> findByExternalLinkId(String externalLink);
}
