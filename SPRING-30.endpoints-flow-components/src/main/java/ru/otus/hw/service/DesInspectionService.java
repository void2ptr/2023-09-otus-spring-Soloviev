package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.ButterflyRepository;
import ru.otus.hw.model.Butterfly;

import java.util.Collection;


@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
@Slf4j
public class DesInspectionService {

    private final InsectEndpoint insectEndpoint;

    private final ButterflyRepository butterflyRepository;

    public void startDesInspection() {
        Collection<Butterfly> butterflies = butterflyRepository.getButterflies();
        Collection<Butterfly> dust = insectEndpoint.startDesInspection(butterflies);
        log.warn(" \nsend:    {}, \nreceive: {}", butterflies, dust);
    }
}
