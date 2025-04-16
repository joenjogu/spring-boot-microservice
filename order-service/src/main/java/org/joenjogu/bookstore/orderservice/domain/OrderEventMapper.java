package org.joenjogu.bookstore.orderservice.domain;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.joenjogu.bookstore.orderservice.domain.model.OrderCreatedEvent;
import org.joenjogu.bookstore.orderservice.domain.model.OrderItem;

public class OrderEventMapper {

    static OrderCreatedEvent toOrderEvent(OrderEntity orderEntity) {
        return new OrderCreatedEvent(
                UUID.randomUUID().toString(),
                orderEntity.getOrderNumber(),
                getOrderItems(orderEntity.getOrderItems()),
                orderEntity.getCustomer(),
                orderEntity.getAddress(),
                LocalDateTime.now());
    }

    private static Set<OrderItem> getOrderItems(Set<OrderItemEntity> orderItems) {
        return orderItems.stream()
                .map(order -> new OrderItem(order.getCode(), order.getName(), order.getPrice(), order.getQuantity()))
                .collect(Collectors.toSet());
    }
}
