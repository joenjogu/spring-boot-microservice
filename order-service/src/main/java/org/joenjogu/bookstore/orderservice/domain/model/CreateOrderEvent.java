package org.joenjogu.bookstore.orderservice.domain.model;

import java.time.LocalDateTime;
import java.util.Set;

public record CreateOrderEvent(
        String eventId,
        String orderNumber,
        Set<OrderItem> orderItems,
        Customer customer,
        Address deliveryAddress,
        LocalDateTime createdAt) {}
