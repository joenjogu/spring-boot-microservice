package org.joenjogu.bookstore.orderservice.domain;

import org.joenjogu.bookstore.orderservice.domain.model.CreateOrderRequest;
import org.joenjogu.bookstore.orderservice.domain.model.CreateOrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public CreateOrderResponse createOrder(String username, CreateOrderRequest orderRequest) {
        OrderEntity newOrderEntity = OrderMapper.toEntity(orderRequest);
        newOrderEntity.setUserName(username);
        OrderEntity saved = orderRepository.save(newOrderEntity);
        log.info("Created new order {}", saved);
        return new CreateOrderResponse(saved.getOrderNumber());
    }
}
