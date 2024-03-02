package ru.otus.hw.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.NullChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import ru.otus.hw.model.Butterfly;


@SuppressWarnings("unused")
@Configuration
public class DesInspectionChannelConfig {
    private static final Integer QUEUE_CAPACITY = 10;

    private static final Long POLLER_PERIOD = 100L;

    private static final Long POLLER_PER_SIZE = 5L;

    @Bean("butterflyInputChannel.input")
    public MessageChannelSpec<?, ?> butterflyInputChannel() {
        return MessageChannels.queue(QUEUE_CAPACITY);
    }

    @Bean("desInspectionChannel.output")
    public MessageChannelSpec<?, ?> desInspectionChannel() {
        return MessageChannels.publishSubscribe();
    }

    @SuppressWarnings("unused")
    @Bean
    public IntegrationFlow desInspectionFlow() {
        return IntegrationFlow
                .from(butterflyInputChannel())
//                .split()
                .channel("from-butterfly-to-null")
                .transform(m -> new NullChannel())
                .transform(new Butterfly(""))
//                .aggregate()
                .channel(desInspectionChannel())
                .get();
    }

}
