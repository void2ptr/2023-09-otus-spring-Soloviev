package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c inner join fetch c.book b where c.book.id = :book_id")
    List<Comment> findByBookId(@Param("book_id") long bookId);

    @Query("select c from Comment c inner join fetch c.book b where c.id = :id")
    Optional<Comment> findById(@Param("id") long commentId);

    @Query("delete from Comment c where c.id = :id")
    void delete(@Param("id") Long commentId);

}
