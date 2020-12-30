package org.mddarr.inventory.service.services;


import org.mddarr.inventory.service.Constants;
import org.mddarr.inventory.service.models.ProductMessage;
import org.mddarr.orders.event.dto.AvroOrder;
import org.mddarr.orders.event.dto.OrderState;

import org.mddarr.products.AvroProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class AvroOrderRequestProducer implements AvroOrderRequestInterface {

    @Autowired
    private KafkaTemplate<String, AvroProduct> kafkaTemplateEvent1;

    private static final Logger logger = LoggerFactory.getLogger(AvroOrderRequestProducer.class);

    public void sendRideRequest(ProductMessage productMessage) {

        AvroProduct product = AvroProduct.newBuilder()
                .setVendor(productMessage.getVendor())
                .setProduct(productMessage.getProduct())
                .setPrice(12.2)
                .setInventory(productMessage.getInventory())
                .build();
        logger.info("Send product to kafka  {}", product);
        kafkaTemplateEvent1.send(Constants.INVENTORY, product);
    }

}
