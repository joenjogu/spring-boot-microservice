package org.joenjogu.bookstore.orderservice.domain;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.joenjogu.bookstore.orderservice.domain.model.OrderCancelledEvent;
import org.joenjogu.bookstore.orderservice.domain.model.OrderCreatedEvent;
import org.joenjogu.bookstore.orderservice.domain.model.OrderDeliveredEvent;
import org.joenjogu.bookstore.orderservice.domain.model.OrderErrorEvent;
import org.joenjogu.bookstore.orderservice.domain.model.OrderItem;

public class OrderEventMapper {

    static OrderCreatedEvent buildOrderCreatedEvent(OrderEntity orderEntity) {
        return new OrderCreatedEvent(
                UUID.randomUUID().toString(),
                orderEntity.getOrderNumber(),
                getOrderItems(orderEntity.getOrderItems()),
                orderEntity.getCustomer(),
                orderEntity.getAddress(),
                LocalDateTime.now());
    }

    static OrderDeliveredEvent buildOrderDeliveredEvent(OrderEntity orderEntity) {
        return new OrderDeliveredEvent(
                UUID.randomUUID().toString(),
                orderEntity.getOrderNumber(),
                getOrderItems(orderEntity.getOrderItems()),
                orderEntity.getCustomer(),
                orderEntity.getAddress(),
                LocalDateTime.now());
    }

    static OrderCancelledEvent buildOrderCancelledEvent(OrderEntity orderEntity, String reason) {
        return new OrderCancelledEvent(
                UUID.randomUUID().toString(),
                orderEntity.getOrderNumber(),
                getOrderItems(orderEntity.getOrderItems()),
                reason,
                orderEntity.getCustomer(),
                orderEntity.getAddress(),
                LocalDateTime.now());
    }

    static OrderErrorEvent buildOrderErrorEvent(OrderEntity orderEntity, String reason) {
        return new OrderErrorEvent(
                UUID.randomUUID().toString(),
                orderEntity.getOrderNumber(),
                getOrderItems(orderEntity.getOrderItems()),
                reason,
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
