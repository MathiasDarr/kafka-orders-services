package org.mddarr.orders.request.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class OrderRequestApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderRequestApplication.class, args);
    }

    private static final Logger logger = LoggerFactory.getLogger(OrderRequestApplication.class);

    @PostConstruct
    public void postInit() {
        logger.info("Application started!");
    }
}
