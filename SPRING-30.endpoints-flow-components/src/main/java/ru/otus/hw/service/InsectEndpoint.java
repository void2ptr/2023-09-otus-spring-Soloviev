package ru.otus.hw.service;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.model.Butterfly;
import ru.otus.hw.model.Caterpillar;

import java.util.Collection;

@MessagingGateway
public interface InsectEndpoint {

    @Gateway(requestChannel = "caterpillarChannel.input", replyChannel = "butterflyChannel.output")
    Collection<Butterfly> startMetamorphoses(Collection<Caterpillar> caterpillar);

}
