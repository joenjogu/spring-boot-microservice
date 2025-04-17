package org.joenjogu.bookstore.orderservice.web;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import java.util.List;
import org.joenjogu.bookstore.orderservice.AbstractIntegrationTest;
import org.joenjogu.bookstore.orderservice.domain.OrderSummary;
import org.joenjogu.bookstore.orderservice.testdata.TestDataFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@Sql("/test-orders.sql")
class OrderControllerTest extends AbstractIntegrationTest {

    @Nested
    class CreateOrderTests {

        @Test
        void shouldCreateOrderSuccessfully() {
            mockGetProductByCode("P100", new BigDecimal("34.00"), "Plastic");

            given().contentType(ContentType.JSON)
                    .body(TestDataFactory.createValidOrderRequest())
                    .when()
                    .post("api/orders")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("orderNumber", notNullValue());
        }

        @Test
        void shouldReturnBadRequestWhenMandatoryDataIsMissing() {
            given().contentType(ContentType.JSON)
                    .body(TestDataFactory.createOrderRequestWithInvalidCustomer())
                    .when()
                    .post("api/orders")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        static void mockGetProductByCode(String productCode, BigDecimal price, String name) {
            stubFor(WireMock.get(urlMatching("/api/products/" + productCode))
                    .willReturn(aResponse()
                            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                            .withStatus(200)
                            .withBody(
                                    """
                                            {
                                            "code": "%s",
                                            "name": "%s",
                                            "price": %f
                                            }
                                            """
                                            .formatted(productCode, name, price.doubleValue()))));
        }
    }

    @Nested
    class GetOrderTests {
        @Test
        void shouldGetOrderSuccessfully() {
            List<OrderSummary> orders = given().when()
                    .get("api/orders")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .body()
                    .as(new TypeRef<>() {});

            assertThat(orders).hasSize(3);
        }
    }

    @Nested
    class GetOrderByNumberTests {

        String orderNumber = "ORD-10002";

        @Test
        void shouldGetOrderSuccessfully() {
            given().when()
                    .get("api/orders/{orderNumber}", orderNumber)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("orderNumber", is(orderNumber))
                    .body("items.size()", is(1));
        }
    }
}
