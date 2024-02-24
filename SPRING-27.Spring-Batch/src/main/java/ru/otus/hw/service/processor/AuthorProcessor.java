package ru.otus.hw.service.processor;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import ru.otus.hw.model.mongo.MongoAuthor;
import ru.otus.hw.model.postgres.Author;
import ru.otus.hw.service.BatchLinkService;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public class AuthorProcessor implements ItemProcessor<Author, MongoAuthor> {

    private final BatchLinkService batchLinkService;

    @Override
    public MongoAuthor process(Author author) {
        MongoAuthor mongoAuthor = new MongoAuthor(author.getFullName());
        batchLinkService.setBatchLinkMap(Author.class.getName(), author.getId().toString(), mongoAuthor);
        return mongoAuthor;
    }

}
