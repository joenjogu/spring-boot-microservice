package org.joenjogu.bookstore.notificationservice.domain;

import org.joenjogu.bookstore.notificationservice.domain.model.OrderEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderEventRepository extends JpaRepository<OrderEventEntity, Long> {
    boolean existsByEventId(String eventId);
}
