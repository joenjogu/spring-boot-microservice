package org.joenjogu.bookstore.orderservice.domain.model;

import java.time.LocalDateTime;
import java.util.Set;

public record OrderCancelledEvent(
        String eventId,
        String orderNumber,
        Set<OrderItem> orderItems,
        String reason,
        Customer customer,
        Address deliveryAddress,
        LocalDateTime createdAt) {}
