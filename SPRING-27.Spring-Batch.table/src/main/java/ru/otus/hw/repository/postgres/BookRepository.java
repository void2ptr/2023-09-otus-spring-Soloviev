package ru.otus.hw.repository.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.model.postgres.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByIdIn(List<Long> id);

}
