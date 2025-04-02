package org.joenjogu.bookstore.orderservice.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.joenjogu.bookstore.orderservice.domain.model.CreateOrderRequest;
import org.joenjogu.bookstore.orderservice.domain.model.OrderItem;

class OrderMapper {

    static OrderEntity toEntity(CreateOrderRequest orderRequest) {
        OrderEntity newOrder = new OrderEntity();
        newOrder.setOrderNumber(UUID.randomUUID().toString());
        newOrder.setStatus(OrderStatus.NEW);
        newOrder.setAddress(orderRequest.deliveryAddress());
        newOrder.setCustomer(orderRequest.customer());
        Set<OrderItemEntity> items = new HashSet<>();
        for (OrderItem item : orderRequest.items()) {
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setCode(item.code());
            orderItem.setName(item.name());
            orderItem.setQuantity(item.quantity());
            orderItem.setPrice(item.price());
            orderItem.setOrder(newOrder);
            items.add(orderItem);
        }
        newOrder.setOrderItems(items);
        return newOrder;
    }
}
