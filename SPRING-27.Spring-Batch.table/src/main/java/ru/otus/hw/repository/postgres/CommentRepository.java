package ru.otus.hw.repository.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.model.postgres.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByIdIn(List<Long> id);

}
