package org.joenjogu.bookstore.orderservice.web.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import org.joenjogu.bookstore.orderservice.domain.OrderStatus;
import org.joenjogu.bookstore.orderservice.domain.model.Address;
import org.joenjogu.bookstore.orderservice.domain.model.Customer;
import org.joenjogu.bookstore.orderservice.domain.model.OrderItem;

public record OrderDTO(
        String orderNumber,
        String user,
        Set<OrderItem> items,
        Customer customer,
        Address deliveryAddress,
        OrderStatus status,
        String comments,
        LocalDateTime createdAt) {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    BigDecimal getTotalAmount() {
        return items.stream()
                .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
