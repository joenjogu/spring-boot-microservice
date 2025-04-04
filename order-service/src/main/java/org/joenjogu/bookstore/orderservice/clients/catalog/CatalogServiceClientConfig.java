package org.joenjogu.bookstore.orderservice.clients.catalog;

import org.joenjogu.bookstore.orderservice.config.ApplicationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class CatalogServiceClientConfig {

    @Bean
    RestClient restClient(ApplicationProperties applicationProperties) {
        return RestClient.builder()
                .baseUrl(applicationProperties.catalogServiceUrl())
                .build();
    }
}
