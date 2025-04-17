package org.joenjogu.bookstore.notificationservice;

import org.springframework.boot.SpringApplication;

public class TestNotificationserviceApplication {

    public static void main(String[] args) {
        SpringApplication.from(NotificationServiceApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }
}
