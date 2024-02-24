package ru.otus.hw.service.processor;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import ru.otus.hw.model.mongo.MongoAuthor;
import ru.otus.hw.model.postgres.Author;
import ru.otus.hw.service.BatchLinkService;

import static ru.otus.hw.config.JobAuthorConfig.IMPORT_AUTHOR_JOB_NAME;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public class AuthorProcessor implements ItemProcessor<Author, MongoAuthor> {

    private final BatchLinkService batchLinkService;

    @Override
    public MongoAuthor process(Author author) {
        MongoAuthor mongoAuthor = new MongoAuthor(author.getFullName());
        batchLinkService.insert(IMPORT_AUTHOR_JOB_NAME, author.getId().toString(), mongoAuthor.getId());
        return mongoAuthor;
    }

}
