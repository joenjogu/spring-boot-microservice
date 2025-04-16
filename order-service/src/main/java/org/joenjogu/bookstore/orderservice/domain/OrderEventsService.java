package org.joenjogu.bookstore.orderservice.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.joenjogu.bookstore.orderservice.domain.model.OrderCancelledEvent;
import org.joenjogu.bookstore.orderservice.domain.model.OrderCreatedEvent;
import org.joenjogu.bookstore.orderservice.domain.model.OrderDeliveredEvent;
import org.joenjogu.bookstore.orderservice.domain.model.OrderErrorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderEventsService {

    private final Logger log = LoggerFactory.getLogger(OrderEventsService.class);

    private final OrderEventRepository orderEventsRepository;
    private final OrderEventPublisher orderPublisher;
    private final ObjectMapper objectMapper;

    public OrderEventsService(
            OrderEventRepository orderEventsRepository, OrderEventPublisher orderPublisher, ObjectMapper objectMapper) {
        this.orderEventsRepository = orderEventsRepository;
        this.orderPublisher = orderPublisher;
        this.objectMapper = objectMapper;
    }

    public void save(OrderCreatedEvent event) {
        OrderEventEntity orderEventEntity = new OrderEventEntity();
        orderEventEntity.setEventId(event.eventId());
        orderEventEntity.setOrderNumber(event.orderNumber());
        orderEventEntity.setEventType(OrderEventType.ORDER_CREATED);
        orderEventEntity.setCreatedAt(event.createdAt());
        orderEventEntity.setPayload(toJson(event));
        orderEventsRepository.save(orderEventEntity);
    }

    public void save(OrderDeliveredEvent event) {
        OrderEventEntity orderEventEntity = new OrderEventEntity();
        orderEventEntity.setEventId(event.eventId());
        orderEventEntity.setOrderNumber(event.orderNumber());
        orderEventEntity.setEventType(OrderEventType.ORDER_CREATED);
        orderEventEntity.setCreatedAt(event.createdAt());
        orderEventEntity.setPayload(toJson(event));
        orderEventsRepository.save(orderEventEntity);
    }

    public void save(OrderCancelledEvent event) {
        OrderEventEntity orderEventEntity = new OrderEventEntity();
        orderEventEntity.setEventId(event.eventId());
        orderEventEntity.setOrderNumber(event.orderNumber());
        orderEventEntity.setEventType(OrderEventType.ORDER_CREATED);
        orderEventEntity.setCreatedAt(event.createdAt());
        orderEventEntity.setPayload(toJson(event));
        orderEventsRepository.save(orderEventEntity);
    }

    public void save(OrderErrorEvent event) {
        OrderEventEntity orderEventEntity = new OrderEventEntity();
        orderEventEntity.setEventId(event.eventId());
        orderEventEntity.setOrderNumber(event.orderNumber());
        orderEventEntity.setEventType(OrderEventType.ORDER_CREATED);
        orderEventEntity.setCreatedAt(event.createdAt());
        orderEventEntity.setPayload(toJson(event));
        orderEventsRepository.save(orderEventEntity);
    }

    public void publishOrderEvents() {
        Sort sort = Sort.by("createdAt").ascending();
        List<OrderEventEntity> orderEventEntities = orderEventsRepository.findAll(sort);
        log.info("Found {} order events to be published", orderEventEntities.size());
        for (OrderEventEntity orderEventEntity : orderEventEntities) {
            publishEvent(orderEventEntity);
            orderEventsRepository.delete(orderEventEntity);
        }
    }

    private void publishEvent(OrderEventEntity orderEventEntity) {
        OrderEventType eventType = orderEventEntity.getEventType();
        switch (eventType) {
            case ORDER_CREATED:
                OrderCreatedEvent orderCreatedEvent = fromJson(orderEventEntity.getPayload(), OrderCreatedEvent.class);
                orderPublisher.publish(orderCreatedEvent);
                break;
            case ORDER_DELIVERED:
                OrderDeliveredEvent orderDeliveredEvent =
                        fromJson(orderEventEntity.getPayload(), OrderDeliveredEvent.class);
                orderPublisher.publish(orderDeliveredEvent);
                break;
            case ORDER_CANCELLED:
                OrderCancelledEvent orderCancelledEvent =
                        fromJson(orderEventEntity.getPayload(), OrderCancelledEvent.class);
                orderPublisher.publish(orderCancelledEvent);
                break;
            case ORDER_PROCESSING_FAILED:
                OrderErrorEvent orderErrorEvent = fromJson(orderEventEntity.getPayload(), OrderErrorEvent.class);
                orderPublisher.publish(orderErrorEvent);
                break;
            default:
                log.error("Unknown event type {}", eventType);
        }
    }

    private String toJson(Object event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T fromJson(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
