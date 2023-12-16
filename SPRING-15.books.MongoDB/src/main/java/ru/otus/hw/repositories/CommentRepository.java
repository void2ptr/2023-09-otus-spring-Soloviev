package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;


public interface CommentRepository extends MongoRepository<Comment, String> {

    @Query("{ 'book.$id' : ObjectId( ?0 ) }")
    Optional<Comment> findByBookId(String bookId);

    @Query("{ 'title' : ?0 }")
    List<Comment> findByBookTitle(String title);

}
