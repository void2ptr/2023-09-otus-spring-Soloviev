package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.ButterflyRepository;
import ru.otus.hw.dao.CaterpillarRepository;
import ru.otus.hw.model.Butterfly;
import ru.otus.hw.model.Caterpillar;

import java.util.List;


@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
@Slf4j
public class InsectService {

    private final InsectEndpoint insectEndpoint;

    private final CaterpillarRepository caterpillarRepository;

    private final ButterflyRepository butterflyRepository;

    public void startMutation() {
        List<Caterpillar> caterpillars = caterpillarRepository.findCaterpillar();
        List<Butterfly> butterflies = insectEndpoint.startMetamorphoses(caterpillars);
        butterflyRepository.saveButterflies(butterflies);

        log.warn(" \nsend:    {}, \nreceive: {}", caterpillars, butterflies);
    }
}
