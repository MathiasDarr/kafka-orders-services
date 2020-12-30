package org.mddarr.inventory.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceApplication.class);

    @PostConstruct
    public void postInit() {
        logger.info("Application started!");
    }
}
