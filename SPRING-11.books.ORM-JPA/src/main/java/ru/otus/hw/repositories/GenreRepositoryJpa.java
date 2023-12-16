package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenreRepositoryJpa implements GenreRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public List<Genre> findAllGenres() {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g", Genre.class);
        return query.getResultList();
    }

    @Override
    public List<Genre> findAllGenresByIds(List<Long> ids) {
        TypedQuery<Genre> query = em.createQuery("SELECT g FROM Genre g WHERE id IN (:ids)", Genre.class);
        query.setParameter("ids", ids);
        return query.getResultList();
    }
}
