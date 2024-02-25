package ru.otus.hw.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.InsectRepository;
import ru.otus.hw.model.Butterfly;
import ru.otus.hw.model.Caterpillar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
@Slf4j
public class InsectService {

    private final InsectEndpoint insectEndpoint;

    private final InsectRepository insectRepository;


    @Getter
    private Collection<Butterfly> butterflies = new ArrayList<>();

    public void startMutation() {
        List<Caterpillar> caterpillars = insectRepository.findAll();
        butterflies = insectEndpoint.startMetamorphoses(caterpillars);
        log.warn(" \nsend:    {}, \nreceive: {}", caterpillars, butterflies);
    }
}
