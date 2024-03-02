package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.hw.config.PropsApplicationData;
import ru.otus.hw.dao.ButterflyRepository;
import ru.otus.hw.model.Butterfly;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;


@SuppressWarnings("unused")
@SpringBootTest
@Import(DesInsectEndpoint.class)
class DesInsectEndpointTest {

    @Autowired
    private DesInsectEndpoint desInsectEndpoint;

    @MockBean
    private PropsApplicationData propsApplicationData;

    @MockBean
    private ButterflyRepository butterflyRepository;

    @Test
    void startDesInspection() {
        List<Butterfly> butterfliesBefore = List.of(new Butterfly("test"));
        given(propsApplicationData.getPathInput()).willReturn("");
        doNothing().when(butterflyRepository).saveButterflies(butterfliesBefore);
        Collection<Butterfly> butterfliesAfter = desInsectEndpoint.startDesInspection(butterfliesBefore);

        assertThat(butterfliesAfter.size()).isEqualTo(0);
    }
}