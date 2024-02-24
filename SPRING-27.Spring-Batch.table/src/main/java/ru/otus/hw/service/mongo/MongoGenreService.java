package ru.otus.hw.service.mongo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.BatchLink;
import ru.otus.hw.model.mongo.MongoGenre;
import ru.otus.hw.repository.BatchLinkRepository;
import ru.otus.hw.repository.mongo.MongoGenreRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static ru.otus.hw.config.JobGenreConfig.IMPORT_GENRE_JOB_NAME;

@SuppressWarnings("unused")
@Slf4j
@Service
@RequiredArgsConstructor
public class MongoGenreService {

    private final MongoGenreRepository mongoGenreRepository;

    private final BatchLinkRepository batchLinkRepository;

    private ConcurrentHashMap<String, MongoGenre> linksCache = new ConcurrentHashMap<>();

    public void fillCache() {
        Map<String, BatchLink> links = batchLinkRepository.findImportedByClassName(IMPORT_GENRE_JOB_NAME).stream()
                .collect(Collectors.toMap(BatchLink::getExportLink, batchLink -> batchLink, (a, b) -> b));
        mongoGenreRepository.findAll().forEach(author -> {
            linksCache.put(links.get(author.getId()).getImportLink(), author);
        });
    }

    public MongoGenre findById(String id) {
        return linksCache.get(id);
    }

}
