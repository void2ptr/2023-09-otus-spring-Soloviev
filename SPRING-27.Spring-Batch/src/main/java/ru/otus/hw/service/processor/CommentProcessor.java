package ru.otus.hw.service.processor;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import ru.otus.hw.model.mongo.MongoBook;
import ru.otus.hw.model.mongo.MongoComment;
import ru.otus.hw.model.postgres.Book;
import ru.otus.hw.model.postgres.Comment;
import ru.otus.hw.service.BatchLinkService;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public class CommentProcessor implements ItemProcessor<Comment, MongoComment> {


    private final BatchLinkService batchLinkService;

    @Override
    public MongoComment process(Comment comment) {
        MongoBook mongoBook = (MongoBook) batchLinkService.getExportLink(
                Book.class.getName(), comment.getBook().getId().toString());
        MongoComment mongoComment = new MongoComment(comment.getDescription(), mongoBook);
        batchLinkService.setBatchLinkMap(Comment.class.getName(), mongoComment.getId(), mongoComment);
        return mongoComment;
    }

}
