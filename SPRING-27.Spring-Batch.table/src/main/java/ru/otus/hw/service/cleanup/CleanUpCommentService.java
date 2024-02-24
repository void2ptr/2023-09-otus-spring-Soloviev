package ru.otus.hw.service.cleanup;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.postgres.Comment;
import ru.otus.hw.repository.postgres.CommentRepository;
import ru.otus.hw.service.BatchLinkService;

import java.util.List;

import static ru.otus.hw.config.JobCommentConfig.IMPORT_COMMENT_JOB_NAME;


@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor
@Service
public class CleanUpCommentService {

    protected final CommentRepository commentRepository;

    protected final BatchLinkService batchLinkService;

    @SuppressWarnings("unused")
    public void cleanUp() {
        log.info("Выполняю завершающие мероприятия...");
        List<Long> exported = batchLinkService.saveAllImported(IMPORT_COMMENT_JOB_NAME);
        List<Comment> authors = commentRepository.findByIdIn(exported);
        authors.forEach(author -> author.setImported(true));
        commentRepository.saveAll(authors);
//        Thread.sleep(1000);
        log.info("Завершающие мероприятия закончены");
    }
}
