package ru.otus.hw.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.hw.service.MetamorphosesService;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;



@SuppressWarnings("unused")
@SpringBootTest
@ContextConfiguration(classes = {DesInspectionChannelConfig.class, InsectChannelConfig.class})
@Import({MetamorphosesService.class})
class DesInspectionChannelConfigTest {


    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @Autowired
    private Map<String, MessageChannel> channels;

    @Test
    void butterflyInputChannel() {
        MessageChannel channel = channels.get("butterflyInputChannel.input");
        MessageHeaders headers = new MessageHeaders(Collections.singletonMap("send", "HEADER"));
        Message<String> message = new GenericMessage<>("TEST", headers);

        boolean send = channel.send(message);
        assertThat(send).isTrue();
    }

    @Test
    void desInspectionChannel() {
        MessageChannel channel = channels.get("desInspectionChannel.output");
        MessageHeaders headers = new MessageHeaders(Collections.singletonMap("send", "HEADER"));
        Message<String> message = new GenericMessage<>("TEST", headers);

        boolean send = channel.send(message);
        assertThat(send).isTrue();
    }

}