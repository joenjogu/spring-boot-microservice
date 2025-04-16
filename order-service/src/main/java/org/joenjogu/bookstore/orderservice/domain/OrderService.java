package org.joenjogu.bookstore.orderservice.domain;

import java.util.List;
import java.util.Optional;
import org.joenjogu.bookstore.orderservice.domain.model.CreateOrderRequest;
import org.joenjogu.bookstore.orderservice.domain.model.CreateOrderResponse;
import org.joenjogu.bookstore.orderservice.web.controller.OrderDTO;
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

    private static final List<String> DELIVERABLE_COUNTRIES = List.of("USA", "INDIA", "GERMANY");

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
        orderEventsService.save(OrderEventMapper.buildOrderCreatedEvent(savedOrder));
        return new CreateOrderResponse(savedOrder.getOrderNumber());
    }

    public List<OrderSummary> getOrdersByUsername(String username) {
        log.info("Service orders by username {}", username);
        return orderRepository.findByUserName(username);
    }

    public Optional<OrderDTO> getOrderByUsernameAndOrderNumber(String username, String orderNumber) {
        return orderRepository
                .findByUsernameAndOrderNumber(username, orderNumber)
                .map(OrderMapper::toDTO);
    }

    public void processNewOrders() {
        List<OrderEntity> newOrders = orderRepository.findByStatus(OrderStatus.NEW);
        log.info("Found {} new orders to process", newOrders.size());
        for (OrderEntity order : newOrders) {
            process(order);
        }
    }

    private void process(OrderEntity order) {
        try {
            if (canBeDelivered(order)) {
                log.info("Delivering order {}", order);
                orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.DELIVERED);
                orderEventsService.save(OrderEventMapper.buildOrderDeliveredEvent(order));
            } else {
                log.info("Order can't be delivered {}", order);
                orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.CANCELLED);
                orderEventsService.save(OrderEventMapper.buildOrderCancelledEvent(
                        order, "Cannot deliver to " + order.getAddress().country()));
            }
        } catch (Exception e) {
            log.error("Error processing order {}", order, e);
            orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.ERROR);
            orderEventsService.save(
                    OrderEventMapper.buildOrderErrorEvent(order, "Error processing order " + order.getOrderNumber()));
        }
    }

    private boolean canBeDelivered(OrderEntity order) {
        return DELIVERABLE_COUNTRIES.contains(order.getAddress().country().toUpperCase());
    }
}
