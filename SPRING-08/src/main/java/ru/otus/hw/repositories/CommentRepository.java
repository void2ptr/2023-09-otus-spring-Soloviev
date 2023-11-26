package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;


public interface CommentRepository extends CrudRepository<Comment, Long> {

//    @Query("select c from Comment c inner join fetch c.book b where c.book.id = :book_id")
//@Param("book_id")
    List<Comment> findByBookId( long bookId);

//    @Query("select c from Comment c inner join fetch c.book b where c.id = :id")
//@Param("id")
    Optional<Comment> findById( long commentId);

//    @Query("delete from Comment c where c.id = :id")
//@Param("id")
    void delete(Long commentId);

}
