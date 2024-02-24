package ru.otus.hw.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.model.mongo.MongoAuthor;

@SuppressWarnings("unused")
public interface MongoAuthorRepository extends MongoRepository<MongoAuthor, String>  {

}
