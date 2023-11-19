package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryJpa implements CommentRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Comment> findByBookId(long bookId) {
        TypedQuery<Comment> query = em.createQuery("""
                select c
                from Comment c
                inner join fetch c.book b
                where c.book.id = :book_id
                """,
                Comment.class);
        query.setParameter("book_id", bookId);
        return query.getResultList();
    }

    @Override
    public Optional<Comment> findById(long commentId) {
        TypedQuery<Comment> query = em.createQuery("""
                select c
                from Comment c
                inner join fetch c.book b
                where c.id = :id
                """,
                Comment.class);
        query.setParameter("id", commentId);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == 0) {
            em.persist(comment);
            return comment;
        }
        return em.merge(comment);
    }

    @Override
    public void delete(Comment comment) {
        Query query = em.createQuery("""
                delete
                from Comment c
                where c.id = :id
                """);
        query.setParameter("id", comment.getId());
        query.executeUpdate();
    }

}
