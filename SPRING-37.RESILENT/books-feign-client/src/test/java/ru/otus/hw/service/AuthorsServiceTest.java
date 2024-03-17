package ru.otus.hw.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.hw.config.WireMockConfig;
import ru.otus.hw.config.WireMockSetup;
import ru.otus.hw.dto.AuthorDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 9091)
@ContextConfiguration(classes = {WireMockConfig.class})
class AuthorsServiceTest {

    private static final String FILE_NAME = "payload/authors-response.json";

    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private AuthorsService authorsService;

    @Autowired
    private ObjectMapper objectMapper;

    private final List<AuthorDto> expected = new ArrayList<>();

    @BeforeEach
    void setUp() throws IOException {
        expected.clear();
        JsonNode parser = new ObjectMapper().readTree(WireMockSetup.class
                .getClassLoader()
                .getResourceAsStream(FILE_NAME));
        parser.forEach(node ->
                expected.add(new AuthorDto(node.get("id").asLong(), node.get("fullName").asText()))
        );
    }

    @Test
    void findAll() throws IOException {
        WireMockSetup.setupMockResponse(wireMockServer, "GET", "/api/v1/authors",
                objectMapper.writeValueAsString(expected));

        assertThat(authorsService.findAll())
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expected);
    }

    @Test
    void findById() throws IOException {
        Long id = 1L;
        AuthorDto authorDto = expected.get(id.intValue());
        WireMockSetup.setupMockResponse(wireMockServer, "GET","/api/v1/authors/" + id,
                objectMapper.writeValueAsString(authorDto));

        assertThat(authorsService.findById(id))
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(authorDto);
    }

    @Test
    void insertAction() throws IOException {
        long id = 20L;
        AuthorDto authorDto = new AuthorDto(id, "New Author");
        WireMockSetup.setupMockResponse(wireMockServer, "POST", "/api/v1/authors",
                objectMapper.writeValueAsString(authorDto));

        assertThat(authorsService.insertAction(authorDto))
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(authorDto);
    }

    @Test
    void updateAction() throws IOException  {
        long id = 2L;
        AuthorDto authorDto = expected.get((int) id);
        WireMockSetup.setupMockResponse(wireMockServer, "PUT", "/api/v1/authors",
                objectMapper.writeValueAsString(authorDto));

        assertThat(authorsService.updateAction(authorDto))
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(authorDto);
    }

    @Test
    void deleteAction() throws IOException  {
        long id = 1L;
        AuthorDto authorDto = expected.get((int) id);
        WireMockSetup.setupMockResponse(wireMockServer, "DELETE","/api/v1/authors/" + id,
                objectMapper.writeValueAsString(authorDto));

        assertThat(authorsService.deleteAction(id))
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(authorDto);
    }
}


