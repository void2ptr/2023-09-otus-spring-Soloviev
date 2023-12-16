package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;


@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public void updateCommentsByBook(Book book) {
        val query = Query.query(Criteria.where("book.$id").is(new ObjectId(book.getId())));
        val update = new Update().set("title", book.getTitle());
        mongoTemplate.findAndModify(query, update, Comment.class);
    }

    @Override
    public void removeCommentsByBookId(String id) {
        val query = Query.query(Criteria.where("book.$id").is(new ObjectId(id)));
        mongoTemplate.findAllAndRemove(query, Comment.class);
    }
}
