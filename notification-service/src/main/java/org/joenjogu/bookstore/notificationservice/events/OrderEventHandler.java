package org.joenjogu.bookstore.notificationservice.events;

import org.joenjogu.bookstore.notificationservice.domain.NotificationService;
import org.joenjogu.bookstore.notificationservice.domain.OrderEventRepository;
import org.joenjogu.bookstore.notificationservice.domain.model.OrderCancelledEvent;
import org.joenjogu.bookstore.notificationservice.domain.model.OrderCreatedEvent;
import org.joenjogu.bookstore.notificationservice.domain.model.OrderDeliveredEvent;
import org.joenjogu.bookstore.notificationservice.domain.model.OrderErrorEvent;
import org.joenjogu.bookstore.notificationservice.domain.model.OrderEventEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventHandler {

    private static final Logger log = LoggerFactory.getLogger(OrderEventHandler.class);

    private final NotificationService notificationService;
    private final OrderEventRepository repository;

    public OrderEventHandler(NotificationService notificationService, OrderEventRepository repository) {
        this.notificationService = notificationService;
        this.repository = repository;
    }

    @RabbitListener(queues = "${notification.new-orders-queue}")
    void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("New order created: {}", event);
        if (repository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate OrderCreatedEvent with id: {}", event.eventId());
            return;
        }
        notificationService.sendOrderCreatedNotification(event);

        OrderEventEntity orderEventEntity = new OrderEventEntity(event.eventId());
        repository.save(orderEventEntity);
    }

    @RabbitListener(queues = "${notification.delivered-orders-queue}")
    void handleOrderDeliveredEvent(OrderDeliveredEvent event) {
        log.info("New order delivered: {}", event);
        if (repository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate OrderDeliveredEvent with id: {}", event.eventId());
            return;
        }
        notificationService.sendOrderDeliveredNotification(event);

        OrderEventEntity orderEventEntity = new OrderEventEntity(event.eventId());
        repository.save(orderEventEntity);
    }

    @RabbitListener(queues = "${notification.cancelled-orders-queue}")
    void handleOrderCancelledEvent(OrderCancelledEvent event) {
        log.info("New order cancelled: {}", event);
        if (repository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate OrderCancelledEvent with id: {}", event.eventId());
            return;
        }
        notificationService.sendOrderCancelledNotification(event);

        OrderEventEntity orderEventEntity = new OrderEventEntity(event.eventId());
        repository.save(orderEventEntity);
    }

    @RabbitListener(queues = "${notification.error-orders-queue}")
    void handleOrderErrorEvent(OrderErrorEvent event) {
        log.info("New order error: {}", event);
        if (repository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate OrderErrorEvent with id: {}", event.eventId());
            return;
        }
        notificationService.sendOrderErrorEventNotification(event);

        OrderEventEntity orderEventEntity = new OrderEventEntity(event.eventId());
        repository.save(orderEventEntity);
    }
}
