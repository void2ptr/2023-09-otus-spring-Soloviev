package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.ButterflyRepository;
import ru.otus.hw.model.Butterfly;

import java.util.List;


@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
@Slf4j
public class DesInspectionService {

    private final DesInsectEndpoint desInsectEndpoint;

    private final ButterflyRepository butterflyRepository;

    public void startDesInspection() {
        List<Butterfly> butterflies = butterflyRepository.findButterflies();
        List<Butterfly> dust = desInsectEndpoint.startDesInspection(butterflies);
        butterflyRepository.saveButterflies(dust);
        log.warn(" \nsend:    {}, \nreceive: {}", butterflies, dust);
    }
}
