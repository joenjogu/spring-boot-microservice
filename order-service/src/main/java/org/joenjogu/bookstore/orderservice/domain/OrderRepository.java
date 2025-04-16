package org.joenjogu.bookstore.orderservice.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByStatus(OrderStatus status);

    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    default void updateOrderStatus(String orderNumber, OrderStatus orderStatus) {
        OrderEntity orderEntity = findByOrderNumber(orderNumber).orElseThrow();
        orderEntity.setStatus(orderStatus);
    }

    @Query(
            """
            select new org.joenjogu.bookstore.orderservice.domain.OrderSummary(o.orderNumber, o.status)
                        from OrderEntity o where o.userName =:userName
            """)
    List<OrderSummary> findByUserName(String userName);

    @Query(
            """
            select distinct o
                        from OrderEntity o left join fetch o.orderItems
                                    where o.userName =:username and o.orderNumber =:orderNumber
            """)
    Optional<OrderEntity> findByUsernameAndOrderNumber(String username, String orderNumber);
}
