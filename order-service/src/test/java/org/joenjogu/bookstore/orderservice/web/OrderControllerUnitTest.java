package org.joenjogu.bookstore.orderservice.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.joenjogu.bookstore.orderservice.domain.OrderService;
import org.joenjogu.bookstore.orderservice.domain.SecurityService;
import org.joenjogu.bookstore.orderservice.domain.model.CreateOrderRequest;
import org.joenjogu.bookstore.orderservice.web.controller.OrderController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.joenjogu.bookstore.orderservice.testdata.TestDataFactory.createOrderRequestWithInvalidAddress;
import static org.joenjogu.bookstore.orderservice.testdata.TestDataFactory.createOrderRequestWithInvalidCustomer;
import static org.joenjogu.bookstore.orderservice.testdata.TestDataFactory.createOrderRequestWithNoItems;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerUnitTest {

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private SecurityService securityService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        given(securityService.getLoginUserName()).willReturn("prasana");
    }

    @ParameterizedTest(name = "[{index}]-{0}")
    @MethodSource("createOrderRequestProvider")
    void shouldReturnBadRequestWhenInvalidOrderRequest(CreateOrderRequest request) throws Exception {
        given(orderService.createOrder(eq("prasana"), any(CreateOrderRequest.class))).willReturn(null);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

    static Stream<Arguments> createOrderRequestProvider() {
        return Stream.of(
                arguments(named("Order with Invalid Customer", createOrderRequestWithInvalidCustomer())),
                arguments(named("Order with Invalid Address", createOrderRequestWithInvalidAddress())),
                arguments(named("Order with No Items", createOrderRequestWithNoItems()))
        );
    }
}
