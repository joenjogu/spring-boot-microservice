package org.joenjogu.bookstore.orderservice.domain;

import org.joenjogu.bookstore.orderservice.domain.model.CreateOrderRequest;
import org.joenjogu.bookstore.orderservice.domain.model.CreateOrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final OrderEventsService orderEventsService;

    public OrderService(
            OrderRepository orderRepository, OrderValidator orderValidator, OrderEventsService orderEventsService) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.orderEventsService = orderEventsService;
    }

    public CreateOrderResponse createOrder(String username, CreateOrderRequest orderRequest) {
        orderValidator.validate(orderRequest);
        OrderEntity newOrderEntity = OrderMapper.toEntity(orderRequest);
        newOrderEntity.setUserName(username);
        OrderEntity savedOrder = orderRepository.save(newOrderEntity);
        log.info("Created new order {}", savedOrder);
        orderEventsService.save(OrderEventMapper.toOrderEvent(savedOrder));
        return new CreateOrderResponse(savedOrder.getOrderNumber());
    }
}
