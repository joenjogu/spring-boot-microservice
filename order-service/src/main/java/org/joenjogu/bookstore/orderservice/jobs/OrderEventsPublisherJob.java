package org.joenjogu.bookstore.orderservice.jobs;

import java.time.Instant;
import org.joenjogu.bookstore.orderservice.domain.OrderEventsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderEventsPublisherJob {

    private static final Logger log = LoggerFactory.getLogger(OrderEventsPublisherJob.class);
    private final OrderEventsService orderEventsService;

    public OrderEventsPublisherJob(OrderEventsService orderEventsService) {
        this.orderEventsService = orderEventsService;
    }

    @Scheduled(cron = "${orders.publish-order-events-cron}")
    public void publishEvent() {
        log.info("Publishing order events at {}", Instant.now());
        orderEventsService.publishOrderEvents();
    }
}
