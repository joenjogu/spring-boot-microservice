package org.joenjogu.bookstore.orderservice.domain;

import org.joenjogu.bookstore.orderservice.clients.catalog.Product;
import org.joenjogu.bookstore.orderservice.clients.catalog.ProductServiceClient;
import org.joenjogu.bookstore.orderservice.domain.model.CreateOrderRequest;
import org.joenjogu.bookstore.orderservice.domain.model.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private static final Logger log = LoggerFactory.getLogger(OrderValidator.class);
    private final ProductServiceClient productServiceClient;

    public OrderValidator(ProductServiceClient productServiceClient) {
        this.productServiceClient = productServiceClient;
    }

    void validate(CreateOrderRequest createOrderRequest) {
        var items = createOrderRequest.items();
        for (OrderItem item : items) {
            Product product = productServiceClient
                    .getProductByCode(item.code())
                    .orElseThrow(() -> new InvalidOrderException("Invalid Product code " + item.code()));

            if (product.price().compareTo(item.price()) != 0) {
                log.error(
                        "Product price does not match: Order price {} vs Actual price {}",
                        item.price(),
                        product.price());
                throw new InvalidOrderException("Product price does not match ");
            }
        }
    }
}
