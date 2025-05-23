package org.joenjogu.bookstore.orderservice.clients.catalog;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ProductServiceClient {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceClient.class);
    private final RestClient restClient;

    public ProductServiceClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @CircuitBreaker(name = "catalog-service")
    @Retry(name = "catalog-service", fallbackMethod = "getProductByCodeFallback")
    public Optional<Product> getProductByCode(String code) {
        log.info("Retrieving product by code {}", code);
        var product =
                restClient.get().uri("api/products/{code}", code).retrieve().body(Product.class);
        log.info("Got product? {}", product);
        return Optional.ofNullable(product);
    }

    private Optional<Product> getProductByCodeFallback(String code, Throwable throwable) {
        log.error("getProductByCodeFallback: {}", code, throwable);
        return Optional.empty();
    }
}
