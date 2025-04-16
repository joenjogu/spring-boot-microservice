package org.joenjogu.bookstore.orderservice.domain;

import org.joenjogu.bookstore.orderservice.config.ApplicationProperties;
import org.joenjogu.bookstore.orderservice.domain.model.OrderCancelledEvent;
import org.joenjogu.bookstore.orderservice.domain.model.OrderCreatedEvent;
import org.joenjogu.bookstore.orderservice.domain.model.OrderDeliveredEvent;
import org.joenjogu.bookstore.orderservice.domain.model.OrderErrorEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ApplicationProperties applicationProperties;

    public OrderEventPublisher(RabbitTemplate rabbitTemplate, ApplicationProperties applicationProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.applicationProperties = applicationProperties;
    }

    public void publish(OrderCreatedEvent orderCreatedEvent) {
        send(applicationProperties.newOrdersQueue(), orderCreatedEvent);
    }

    public void publish(OrderDeliveredEvent orderDeliveredEvent) {
        send(applicationProperties.deliveredOrdersQueue(), orderDeliveredEvent);
    }

    public void publish(OrderCancelledEvent orderCancelledEvent) {
        send(applicationProperties.cancelledOrdersQueue(), orderCancelledEvent);
    }

    public void publish(OrderErrorEvent orderErrorEvent) {
        send(applicationProperties.errorOrdersQueue(), orderErrorEvent);
    }

    private void send(String routingKey, Object payload) {
        rabbitTemplate.convertAndSend(applicationProperties.orderEventsExchange(), routingKey, payload);
    }
}
