package org.joenjogu.bookstore.orderservice;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.wiremock.integrations.testcontainers.WireMockContainer;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIntegrationTest {
    @LocalServerPort
    int port;

    static WireMockContainer container = new WireMockContainer("wiremock/wiremock:3.5.2-alpine");

    @BeforeAll
    static void beforeAll() {
        container.start();
        configureFor(container.getHost(), container.getPort());
    }

    @DynamicPropertySource
    static void configuration(final DynamicPropertyRegistry registry) {
        registry.add("orders.catalog-service-url", container::getBaseUrl);
    }

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }
}
