package ru.otus.hw.service.processor;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import ru.otus.hw.model.mongo.MongoBook;
import ru.otus.hw.model.mongo.MongoComment;
import ru.otus.hw.model.postgres.Comment;
import ru.otus.hw.service.BatchLinkService;
import ru.otus.hw.service.mongo.MongoBookService;

import static ru.otus.hw.config.JobCommentConfig.IMPORT_COMMENT_JOB_NAME;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public class CommentProcessor implements ItemProcessor<Comment, MongoComment> {

    private final BatchLinkService batchLinkService;

    private final MongoBookService mongoBookService;


    @Override
    public MongoComment process(Comment comment) {
        MongoBook mongoBook = mongoBookService.findById(comment.getBook().getId().toString());
        MongoComment mongoComment = new MongoComment(comment.getDescription(), mongoBook);
        batchLinkService.insert(IMPORT_COMMENT_JOB_NAME, comment.getId().toString(), mongoComment.getId());
        return mongoComment;
    }

}
