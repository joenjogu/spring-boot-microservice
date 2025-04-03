package org.joenjogu.bookstore.orderservice.web;

import io.restassured.http.ContentType;
import org.joenjogu.bookstore.orderservice.AbstractIntegrationTest;
import org.joenjogu.bookstore.orderservice.testdata.TestDataFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

class OrderControllerTest extends AbstractIntegrationTest {

    @Nested
    class CreateOrderTests {

        @Test
        void shouldCreateOrderSuccessfully() {
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
    }
}
