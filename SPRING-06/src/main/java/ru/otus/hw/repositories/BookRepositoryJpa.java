package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookRepositoryJpa implements BookRepository {

    @PersistenceContext
    private EntityManager em;


    @Override
    public Optional<Book> findById(long id) {
        TypedQuery<Book> query = em.createQuery("""
                select b
                  from Book b
                  left join fetch b.author a
                  left join fetch b.genres g
                 where b.id = :id
                """,
                Book.class);
        query.setParameter("id", id);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public List<Book> findAll() {
        TypedQuery<Book> query = em.createQuery("""
                select b
                  from Book b
                  left join fetch b.author a
                  left join fetch b.genres g
                """,
                Book.class);
        return query.getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            em.persist(book);
            return book;
        }
        return em.merge(book);
    }

    @Override
    public void deleteById(long id) {
        //...
        Optional<Book> bookOptional = findById(id);

        if (bookOptional.isPresent()) {
            removeGenresRelationsFor(bookOptional.get());
            Query query = em.createQuery("""
                        delete
                          from Book b
                         where b.id = :book_id
                        """);
            query.setParameter("book_id", id);
            query.executeUpdate();
        }
    }

    private void removeGenresRelationsFor(Book book) {
        //...
        Query query = em.createQuery("""
                        delete
                          from BookGenre bg
                         where bg.bookId = :book_id
                        """);
        query.setParameter("book_id", book.getId());
        query.executeUpdate();
    }

}
