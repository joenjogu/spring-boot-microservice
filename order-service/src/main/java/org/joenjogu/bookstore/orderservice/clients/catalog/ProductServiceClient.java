package org.joenjogu.bookstore.orderservice.clients.catalog;

import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ProductServiceClient {

    private final RestClient restClient;

    public ProductServiceClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public Optional<Product> getProductByCode(String code) {
        try {
            var product =
                    restClient.get().uri("api/products/{code}", code).retrieve().body(Product.class);

            return Optional.ofNullable(product);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
