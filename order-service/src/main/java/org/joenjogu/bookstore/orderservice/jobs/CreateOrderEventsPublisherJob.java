package org.joenjogu.bookstore.orderservice.jobs;

import java.time.Instant;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.joenjogu.bookstore.orderservice.domain.OrderEventsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CreateOrderEventsPublisherJob {

    private static final Logger log = LoggerFactory.getLogger(CreateOrderEventsPublisherJob.class);
    private final OrderEventsService orderEventsService;

    public CreateOrderEventsPublisherJob(OrderEventsService orderEventsService) {
        this.orderEventsService = orderEventsService;
    }

    @Scheduled(cron = "${orders.publish-order-creation-events-cron}")
    @SchedulerLock(name = "publishCreateOrderEvent")
    public void publishCreateOrderEvent() {
        LockAssert.assertLocked();
        log.info("Publishing create order event at {}", Instant.now());
        orderEventsService.publishOrderEvents();
    }
}
