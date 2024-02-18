package ru.otus.hw.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.hw.mapper.CocoonMapper;
import ru.otus.hw.service.MetamorphosesService;


@Configuration
public class InsectChannelConfig {
    private static final Integer QUEUE_CAPACITY = 10;

    private static final Long POLLER_PERIOD = 100L;

    private static final Long POLLER_PER_SIZE = 5L;

    @Bean("caterpillarChannel.input")
    public MessageChannelSpec<?, ?> caterpillarChannel() {
        return MessageChannels.queue(QUEUE_CAPACITY);
    }

    @Bean("butterflyChannel.output")
    public MessageChannelSpec<?, ?> butterflyChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(POLLER_PERIOD).maxMessagesPerPoll(POLLER_PER_SIZE);
    }

    @Bean
    public IntegrationFlow transformationFlow(MetamorphosesService metamorphosesService) {
        return IntegrationFlow
                .from(caterpillarChannel())
                .split()
                .channel("from-caterpillar-to-cocoon")
                .handle(metamorphosesService,"startCocoon")
                .channel("from-cocoon-to-pupa")
                .transform(CocoonMapper::toPupa)
                .channel("from-pupa-to-butterfly")
                .handle(metamorphosesService,"startButterfly")
                .aggregate()
                .channel(butterflyChannel())
                .get();
    }

}
