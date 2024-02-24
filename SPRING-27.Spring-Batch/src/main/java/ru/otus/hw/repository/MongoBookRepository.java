package ru.otus.hw.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.model.mongo.MongoBook;

@SuppressWarnings("unused")
public interface MongoBookRepository extends MongoRepository<MongoBook, String> {

}
