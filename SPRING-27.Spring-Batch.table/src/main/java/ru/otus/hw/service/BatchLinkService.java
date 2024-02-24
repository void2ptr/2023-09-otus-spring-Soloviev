package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.BatchLink;
import ru.otus.hw.repository.BatchLinkRepository;

import java.util.List;


@SuppressWarnings("unused")
@Slf4j
@Service
@RequiredArgsConstructor
public class BatchLinkService {

    private final BatchLinkRepository batchLinkRepository;

    public void insert(String className, String importId, String exportId) {
        batchLinkRepository.save(new BatchLink(className, importId, exportId));
    }

    public List<BatchLink> findByClassName(String className) {
        return batchLinkRepository.findNotImportedByClassName(className);
    }

    public List<Long> saveAllImported(String importJobName) {
        List<BatchLink> batchLinks = batchLinkRepository.findNotImportedByClassName(importJobName);
        batchLinks.forEach(batchLink -> batchLink.setImported(true));
        return batchLinkRepository.saveAll(batchLinks).stream()
                .map(batchLink -> Long.valueOf(batchLink.getImportLink()))
                .toList();
    }

//    public Optional<BatchLink> findByImportLink(String className, String id) {
//        return batchLinkRepository.findByImportLink(className, id);
//    }

}
