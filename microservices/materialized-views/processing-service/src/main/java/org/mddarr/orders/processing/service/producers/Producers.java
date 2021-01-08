package org.mddarr.orders.processing.service.producers;

import lombok.RequiredArgsConstructor;
import org.mddarr.orders.processing.service.Constants;
import org.mddarr.orders.processing.service.templates.KafkaGenericTemplate;
import org.mddarr.products.AvroInventory;
import org.mddarr.products.AvroPurchaseCount;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Configuration
public class Producers {

    @Component
    @RequiredArgsConstructor
    class AvroProdProducer {

        @EventListener(ApplicationStartedEvent.class)
        public void produce() {

            KafkaGenericTemplate<AvroPurchaseCount> kafkaGenericTemplate = new KafkaGenericTemplate<>();
            KafkaTemplate<String, AvroPurchaseCount> purchaseEventKafkaTemplate = kafkaGenericTemplate.getKafkaTemplate();
            purchaseEventKafkaTemplate.setDefaultTopic(Constants.PURCHASE_COUNT_TOPIC);

            AvroPurchaseCount avroPurchaseCount = AvroPurchaseCount.newBuilder()
                    .setProductId("product1")
                    .setCount(120)
                    .build();

            purchaseEventKafkaTemplate.sendDefault(avroPurchaseCount.getProductId(),avroPurchaseCount);
        }
    }



    @Component
    @RequiredArgsConstructor
    class InventoryProducer {


        @EventListener(ApplicationStartedEvent.class)
        public void produce() {

            KafkaGenericTemplate<AvroInventory> kafkaGenericTemplate = new KafkaGenericTemplate<>();
            KafkaTemplate<String, AvroInventory> purchaseEventKafkaTemplate = kafkaGenericTemplate.getKafkaTemplate();
            purchaseEventKafkaTemplate.setDefaultTopic(Constants.INVENTORY_TOPIC);

            AvroInventory avroInventory = AvroInventory.newBuilder()
                    .setProductid("product1")
                    .setInventory(100)
                    .build();

            purchaseEventKafkaTemplate.sendDefault(avroInventory.getProductid(),avroInventory);
        }
    }

}
