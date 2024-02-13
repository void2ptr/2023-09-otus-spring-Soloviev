package ru.otus.hw.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import ru.otus.hw.model.mongo.MongoGenre;

import java.util.Optional;

public interface MongoGenreRepository extends MongoRepository<MongoGenre, String> {

    @Query("{ 'externalLink' : ?0 }")
    Optional<MongoGenre> findByExternalLinkId(String externalLink);

}
