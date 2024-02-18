package ru.otus.hw.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.hw.service.MetamorphosesService;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ContextConfiguration(classes = InsectChannelConfig.class)
@Import({MetamorphosesService.class})
class InsectChannelConfigTest {

    @Autowired
    private ApplicationContext ctx;

    private Map<String, MessageChannel> channels;

    @BeforeEach
    public void setUp(){
        channels = ctx.getBeansOfType(MessageChannel.class);
    }
    @Test
    void caterpillarChannel() {
        MessageChannel channel = channels.get("caterpillarChannel.input");
        MessageHeaders headers = new MessageHeaders(Collections.singletonMap("send", "HEADER"));
        Message<String> message = new GenericMessage<>("TEST", headers);

        boolean send = channel.send(message);
        assertThat(send).isTrue();
    }

    @Test
    void butterflyChannel() {
        MessageChannel channel = channels.get("butterflyChannel.output");
        MessageHeaders headers = new MessageHeaders(Collections.singletonMap("send", "HEADER"));
        Message<String> message = new GenericMessage<>("TEST", headers);

        boolean send = channel.send(message);
        assertThat(send).isTrue();
    }

    @Test
    void poller() {
        Map<String, PollerSpec> pollerChannels = ctx.getBeansOfType(PollerSpec.class);
        PollerSpec poller = pollerChannels.get("&" + PollerMetadata.DEFAULT_POLLER);
        assertThat(poller).isNotNull();
    }

}