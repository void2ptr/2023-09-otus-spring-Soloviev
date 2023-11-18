package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryJpa implements CommentRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Comment> findCommentByBookId(long id) {
        TypedQuery<Comment> query = em.createQuery("""
                select c 
                from Comment c
                join fetch Book b   
                where c.book.id = :book_id
                """,
                Comment.class);
        query.setParameter("book_id", id);
        return query.getResultList();
    }

}
