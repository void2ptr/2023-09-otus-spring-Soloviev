package ru.otus.hw.service;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.model.Butterfly;
import ru.otus.hw.model.Caterpillar;

import java.util.List;

@MessagingGateway
public interface InsectEndpoint {

    @SuppressWarnings("unused")
    @Gateway(requestChannel = "caterpillarChannel.input", replyChannel = "butterflyChannel.output")
    List<Butterfly> startMetamorphoses(List<Caterpillar> caterpillar);

}
