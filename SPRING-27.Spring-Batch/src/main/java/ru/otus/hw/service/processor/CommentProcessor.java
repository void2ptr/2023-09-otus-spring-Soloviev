package ru.otus.hw.service.processor;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import ru.otus.hw.model.mongo.MongoBook;
import ru.otus.hw.model.mongo.MongoComment;
import ru.otus.hw.model.postgres.Comment;
import ru.otus.hw.service.MongoBookService;


@RequiredArgsConstructor
public class CommentProcessor implements ItemProcessor<Comment, MongoComment> {

    private final MongoBookService mongoBookService;

    @Override
    public MongoComment process(Comment comment) throws Exception {
        MongoBook mongoBook = mongoBookService.getMongoBook(comment.getBook().getId().toString());
        return new MongoComment(comment.getDescription(),
                mongoBook,
                comment.getId().toString()
        );
    }

}
