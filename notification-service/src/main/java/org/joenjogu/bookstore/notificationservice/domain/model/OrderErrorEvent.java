package org.joenjogu.bookstore.notificationservice.domain.model;

import java.time.LocalDateTime;
import java.util.Set;

public record OrderErrorEvent(
        String eventId,
        String orderNumber,
        Set<OrderItem> orderItems,
        String reason,
        Customer customer,
        Address deliveryAddress,
        LocalDateTime createdAt) {}
