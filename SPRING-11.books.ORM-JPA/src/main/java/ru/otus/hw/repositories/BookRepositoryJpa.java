package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;


@Repository
@RequiredArgsConstructor
public class BookRepositoryJpa implements BookRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Optional<Book> findBookById(long id) {
        var entityGraph = em.getEntityGraph("book-author-genres-entity-graph");
        Map<String, Object> properties = new HashMap<>();
        properties.put(FETCH.getKey(), entityGraph);
        Book book = em.find(Book.class, id, properties);
        return Optional.ofNullable(book);
    }

    @Override
    public List<Book> findAllBooks() {
        var entityGraph = em.getEntityGraph("book-author-entity-graph");
        TypedQuery<Book> query = em.createQuery("select b from Book b", Book.class);
        query.setHint(FETCH.getKey(), entityGraph);
        return query.getResultList();
    }

    @Override
    public Book saveBook(Book book) {
        if (book.getId() == 0) {
            em.persist(book);
            return book;
        }
        return em.merge(book);
    }

    @Override
    public void deleteBookById(long id) {
        Optional<Book> bookOptional = findBookById(id);

        bookOptional.ifPresent(em::remove);
    }
}
