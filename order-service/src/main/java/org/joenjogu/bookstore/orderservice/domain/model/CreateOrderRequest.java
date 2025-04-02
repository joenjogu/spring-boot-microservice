package org.joenjogu.bookstore.orderservice.domain.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record CreateOrderRequest(
        @Valid @NotEmpty(message = "Items cannot be empty") Set<OrderItem> items,
        @Valid Address deliveryAddress,
        @Valid Customer customer) {}
