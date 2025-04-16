package org.joenjogu.bookstore.orderservice.web.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.joenjogu.bookstore.orderservice.domain.OrderNotFoundException;
import org.joenjogu.bookstore.orderservice.domain.OrderService;
import org.joenjogu.bookstore.orderservice.domain.OrderSummary;
import org.joenjogu.bookstore.orderservice.domain.SecurityService;
import org.joenjogu.bookstore.orderservice.domain.model.CreateOrderRequest;
import org.joenjogu.bookstore.orderservice.domain.model.CreateOrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final SecurityService securityService;

    public OrderController(OrderService orderService, SecurityService securityService) {
        this.orderService = orderService;
        this.securityService = securityService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    CreateOrderResponse createOrder(@Valid @RequestBody CreateOrderRequest orderRequest) {
        String username = securityService.getLoginUserName();
        CreateOrderResponse createdOrder = orderService.createOrder(username, orderRequest);
        log.info("Order created {}", createdOrder);
        return createdOrder;
    }

    @GetMapping()
    List<OrderSummary> getOrder() {
        String username = securityService.getLoginUserName();
        log.info("Get orders by username {}", username);
        return orderService.getOrdersByUsername(username);
    }

    @GetMapping("/{orderNumber}")
    OrderDTO getOrder(@PathVariable String orderNumber) {
        String username = securityService.getLoginUserName();
        return orderService
                .getOrderByUsernameAndOrderNumber(username, orderNumber)
                .orElseThrow(() -> new OrderNotFoundException(orderNumber));
    }
}
