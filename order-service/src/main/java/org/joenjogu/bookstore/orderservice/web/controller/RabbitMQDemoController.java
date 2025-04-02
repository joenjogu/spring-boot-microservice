package org.joenjogu.bookstore.orderservice.web.controller;

import org.joenjogu.bookstore.catalog.domain.MessageRequest;
import org.joenjogu.bookstore.orderservice.config.ApplicationProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RabbitMQDemoController {

    private final RabbitTemplate rabbitTemplate;
    private final ApplicationProperties applicationProperties;

    public RabbitMQDemoController(RabbitTemplate rabbitTemplate, ApplicationProperties applicationProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.applicationProperties = applicationProperties;
    }

    @PostMapping("/send")
    public void send(@RequestBody MessageRequest message) {
        rabbitTemplate.convertAndSend(applicationProperties.orderEventsExchange(), message.queueName, message.payload);
    }
}
