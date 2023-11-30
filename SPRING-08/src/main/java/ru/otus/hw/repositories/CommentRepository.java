package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;


public interface CommentRepository extends MongoRepository<Comment, String> {

//    @Query("select c from Comment c inner join fetch c.book b where c.book.id = :book_id")
//@Param("book_id")
    List<Comment> findByBookId(String bookId);

//    @Query("select c from Comment c inner join fetch c.book b where c.id = :id")
//@Param("id")
    Optional<Comment> findById(String commentId);

//    @Query("delete from Comment c where c.id = :id")
//@Param("id")
    void deleteById(String Id);

}
