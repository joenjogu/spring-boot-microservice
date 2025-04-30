package org.joenjogu.bookstore.notificationservice.domain;

import jakarta.mail.internet.MimeMessage;
import org.joenjogu.bookstore.notificationservice.config.ApplicationProperties;
import org.joenjogu.bookstore.notificationservice.domain.model.OrderCancelledEvent;
import org.joenjogu.bookstore.notificationservice.domain.model.OrderCreatedEvent;
import org.joenjogu.bookstore.notificationservice.domain.model.OrderDeliveredEvent;
import org.joenjogu.bookstore.notificationservice.domain.model.OrderErrorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final ApplicationProperties properties;
    private final JavaMailSender mailSender;

    public NotificationService(ApplicationProperties properties, JavaMailSender mailSender) {
        this.properties = properties;
        this.mailSender = mailSender;
    }

    public void sendOrderCreatedNotification(OrderCreatedEvent orderCreatedEvent) {
        String message =
                """
                ===================================================
                Order Created Notification
                ----------------------------------------------------
                Dear %s,
                Your Order Number: %s has been created successfully.

                Thanks,
                BookStore Team
                ===================================================
                """
                        .formatted(orderCreatedEvent.customer().name(), orderCreatedEvent.orderNumber());
        log.info("\n{}", message);
        sendEmail(orderCreatedEvent.customer().email(), "Order Created Notification", message);
    }

    public void sendOrderDeliveredNotification(OrderDeliveredEvent event) {
        String message =
                """
                ===================================================
                Order Delivered Notification
                ----------------------------------------------------
                Dear %s,
                Your Order Number: %s has been delivered successfully.

                Thanks,
                BookStore Team
                ===================================================
                """
                        .formatted(event.customer().name(), event.orderNumber());
        log.info("\n{}", message);
        sendEmail(event.customer().email(), "Order Delivered Notification", message);
    }

    public void sendOrderCancelledNotification(OrderCancelledEvent event) {
        String message =
                """
                ===================================================
                Order Cancelled Notification
                ----------------------------------------------------
                Dear %s,
                Your order with orderNumber: %s has been cancelled.
                Reason: %s

                Thanks,
                BookStore Team
                ===================================================
                """
                        .formatted(event.customer().name(), event.orderNumber(), event.reason());
        log.info("\n{}", message);
        sendEmail(event.customer().email(), "Order Cancelled Notification", message);
    }

    public void sendOrderErrorEventNotification(OrderErrorEvent event) {
        String message =
                """
                ===================================================
                Order Processing Failure Notification
                ----------------------------------------------------
                Hi Team,
                The order processing failed for orderNumber: %s.
                Reason: %s

                Thanks,
                BookStore Team
                ===================================================
                """
                        .formatted(event.orderNumber(), event.reason());
        log.info("\n{}", message);
        sendEmail(properties.supportEmail(), "Order Processing Failure Notification", message);
    }

    private void sendEmail(String recipient, String subject, String content) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
        try {
            mimeMessageHelper.setFrom(properties.supportEmail());
            mimeMessageHelper.setTo(recipient);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content);
            mailSender.send(mimeMessage);
            log.info("Email successfully sent to : {}", recipient);
        } catch (Exception e) {
            throw new RuntimeException("Error sending email to " + recipient, e);
        }
    }
}
