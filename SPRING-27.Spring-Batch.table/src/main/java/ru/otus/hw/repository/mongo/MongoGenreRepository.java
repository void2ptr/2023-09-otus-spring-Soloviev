package ru.otus.hw.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.model.mongo.MongoGenre;

@SuppressWarnings("unused")
public interface MongoGenreRepository extends MongoRepository<MongoGenre, String> {

}
