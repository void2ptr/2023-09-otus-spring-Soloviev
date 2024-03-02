package ru.otus.hw.service;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.model.Butterfly;

import java.util.List;

@MessagingGateway
public interface DesInsectEndpoint {

    @SuppressWarnings("unused")
    @Gateway(requestChannel = "butterflyInputChannel.input", replyChannel = "desInspectionChannel.output")
    List<Butterfly> startDesInspection(List<Butterfly> butterflies);

}
