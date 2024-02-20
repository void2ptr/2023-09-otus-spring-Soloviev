package ru.otus.hw.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.model.mongo.MongoComment;

@SuppressWarnings("unused")
public interface MongoCommentRepository extends MongoRepository<MongoComment, String> {
}
