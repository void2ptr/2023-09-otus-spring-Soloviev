package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
@RequiredArgsConstructor
public class BookRepositoryJpa implements BookRepository {

    @PersistenceContext
    final private EntityManager em;

    @Override
    public Optional<Book> findById(long id) {
        Book book = em.find(Book.class, id);
        return book == null ? Optional.empty() : Optional.of(book);
    }

    @Override
    public List<Book> findAll() {
//        TypedQuery<Book> query = em.createQuery("""
//                select b
//                  from Book b
//                  left join fetch b.author a
//                  left join fetch b.genres g
//                """,
//                Book.class);
//        return query.getResultList();

        var books = getAllBooksWithoutGenres();
//        var genres = em.createQuery("select g from Genre g", Genre.class).getResultList();
//        var relations = getAllGenreRelations();

//        mergeBooksInfo(books, genres, relations);
        return books;
    }

    private List<Book> getAllBooksWithoutGenres() {
//        return jdbc.query("""
//                        SELECT b.id        as book_id
//                             , b.title     AS book_title
//                             , a.id        AS author_id
//                             , a.full_name AS author_name
//                          FROM      books   b
//                         INNER JOIN authors a ON a.id       = b.author_id
//                        """,
//                new BookMapper()
//        );

//        EntityGraph<?> entityGraph = em.getEntityGraph("book-author-entity-graph");
//        Map<String, Object> properties = new HashMap<>();
//        properties.put(FETCH.getKey(), entityGraph);
//        return Optional.ofNullable(em.find(Book.class, id, properties )); //—-тут пропертис удалил и забыл

        EntityGraph<?> entityGraph = em.getEntityGraph("book-author-entity-graph");
        TypedQuery<Book> query = em.createQuery("select distinct b from Book b " +
                "left join fetch b.author a", Book.class);
        query.setHint(FETCH.getKey(), entityGraph);
        return query.getResultList();

//        return query.getResultStream()
//                .collect(
//                        Collectors.toMap(
//                                tuple -> ((Long) tuple.get("id")).longValue(),
//                                tuple -> (Book) tuple.get("book_id")
//                        )
//                );
    }
/*
    @SuppressWarnings("unchecked")
    private List<BookGenre> getAllGenreRelations() {
//        return jdbc.query("SELECT book_id, genre_id FROM books_genres", new BookGenreRowMapper());

        return em.createNativeQuery("SELECT book_id, genre_id FROM books_genres",
                BookGenre.class).getResultList();
//        Map<Long, BookGenre> relations =  em.createQuery("SELECT book_id, genre_id FROM books_genres",
//                        Tuple.class)
//                .getResultStream()
//                .collect(
//                        Collectors.toMap(
//                                tuple -> ((Long) tuple.get("book_id")).longValue(),
//                                tuple -> (BookGenre) tuple.get("book_id")
//                        )
//                );
//        return relations;
    }
*/
//    private void mergeBooksInfo(List<Book> booksWithoutGenres,
//                                List<Genre> genres,
//                                List<BookGenre> relations) {
//        // Добавить книгам (booksWithoutGenres) жанры (genres) в соответствии со связями (relations)
//        Map<Long, Book> mapBooks = booksWithoutGenres.stream()
//                .collect(Collectors.toMap(Book::getId, book -> book, (a, b) -> b));
//        Map<Long, Genre> mapGenre = genres.stream()
//                .collect(Collectors.toMap(Genre::getId, genre -> genre, (a, b) -> b));
//
//        relations.forEach(relation -> mapBooks.get(relation.getBookId())
//                .getGenres().add(mapGenre.get(relation.getGenreId())));
//    }


//    private record BookGenreRelation(long bookId, long genreId) {
//    }
//
//    private static class BookGenreRowMapper implements RowMapper<BookGenreRelation> {
//
//        @Override
//        public BookGenreRelation mapRow(ResultSet rs, int i) throws SQLException {
//            long bookId = rs.getLong("book_id");
//            long genreId = rs.getLong("genre_id");
//            return new BookGenreRelation(bookId, genreId);
//        }
//    }

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
        Optional<Book> bookOptional = findById(id);

        if (bookOptional.isPresent()) {
            em.remove(bookOptional.get());
            em.flush();
            em.clear();
        }
    }
}
