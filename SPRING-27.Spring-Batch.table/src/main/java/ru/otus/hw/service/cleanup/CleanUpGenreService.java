package ru.otus.hw.service.cleanup;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.postgres.Genre;
import ru.otus.hw.repository.postgres.GenreRepository;
import ru.otus.hw.service.BatchLinkService;

import java.util.List;

import static ru.otus.hw.config.JobGenreConfig.IMPORT_GENRE_JOB_NAME;


@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor
@Service
public class CleanUpGenreService {

    protected final GenreRepository genreRepository;

    protected final BatchLinkService batchLinkService;


    @SuppressWarnings("unused")
    public void cleanUp() {
        log.info("Выполняю завершающие мероприятия...");
        List<Long> exported = batchLinkService.saveAllImported(IMPORT_GENRE_JOB_NAME);
        List<Genre> authors = genreRepository.findByIdIn(exported);
        authors.forEach(author -> author.setImported(true));
        genreRepository.saveAll(authors);
//        Thread.sleep(1000);
        log.info("Завершающие мероприятия закончены");
    }
}
