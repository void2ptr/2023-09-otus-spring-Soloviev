package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.hw.config.props.InsectsRepositoryProps;
import ru.otus.hw.config.InsectChannelConfig;
import ru.otus.hw.dao.CaterpillarRepository;
import ru.otus.hw.model.Butterfly;
import ru.otus.hw.model.Caterpillar;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("unused")
@SpringBootTest
@Import(InsectChannelConfig.class)
class InsectEndpointTest {

    @Autowired
    private InsectEndpoint insectEndpoint;

    @MockBean
    private InsectsRepositoryProps insectsRepositoryProps;

    @MockBean
    private CaterpillarRepository caterpillarRepository;

    @Test
    void startMetamorphoses() {
        List<Caterpillar> caterpillars = List.of(new Caterpillar("TEST"));
        given(caterpillarRepository.findCaterpillar()).willReturn(caterpillars);
        given(insectsRepositoryProps.getPathInput()).willReturn("");

        Collection<Butterfly> butterflies = insectEndpoint.startMetamorphoses(caterpillars);

        assertThat(butterflies.size()).isEqualTo(1);
        butterflies.forEach( b ->
                assertThat(b.getKind()).isEqualTo("test")
        );

    }
}