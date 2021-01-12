package org.mddarr.ordersviews.producers;

import lombok.RequiredArgsConstructor;
import org.mddarr.ordersviews.Constants;
import org.mddarr.ordersviews.templates.KafkaGenericTemplate;
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
    class OrdersProducer {

        private final KafkaTemplate<Integer, String> kafkaTemplate;
        @EventListener(ApplicationStartedEvent.class)
        public void produce() {
            kafkaTemplate.send(Constants.ORDERS_TOPIC, 1, "iPad");
            kafkaTemplate.send(Constants.ORDERS_TOPIC, 2, "iPhone");
            kafkaTemplate.send(Constants.ORDERS_TOPIC, 1, "iPad, Airpods");
            kafkaTemplate.send(Constants.ORDERS_TOPIC, 2, "HomePod");
        }
    }

    @Component
    @RequiredArgsConstructor
    class InventoryProducer {

        private final KafkaTemplate<Integer, String> kafkaTemplate;

        @EventListener(ApplicationStartedEvent.class)
        public void produce() {

            KafkaGenericTemplate<AvroInventory> kafkaGenericTemplate = new KafkaGenericTemplate<>();
            KafkaTemplate<String, AvroInventory> purchaseEventKafkaTemplate = kafkaGenericTemplate.getKafkaTemplate();
            purchaseEventKafkaTemplate.setDefaultTopic(Constants.PRODUCT_INVENTORY_TOPIC_STRING);

            AvroInventory avroInventory = AvroInventory.newBuilder()
                    .setInventory(12)
                    .setProductid("product1")
                    .build();

            purchaseEventKafkaTemplate.sendDefault(avroInventory.getProductid(), avroInventory);
        }
    }
//
//


//    @Component
//    @RequiredArgsConstructor
//    class PurchaseCountProducer {
//
//        private final KafkaTemplate<Integer, String> kafkaTemplate;
//
//        @EventListener(ApplicationStartedEvent.class)
//        public void produce() {
//
//            KafkaGenericTemplate<AvroPurchaseCount> kafkaGenericTemplate = new KafkaGenericTemplate<>();
//            KafkaTemplate<String, AvroPurchaseCount> purchaseEventKafkaTemplate = kafkaGenericTemplate.getKafkaTemplate();
//            purchaseEventKafkaTemplate.setDefaultTopic(Constants.PURCHASE_COUNT_TOPIC);
//
//            AvroPurchaseCount avroPurchaseCount = AvroPurchaseCount.newBuilder()
//                    .setProductId("product1")
//                    .setCount(120)
//                    .build();
//
//            purchaseEventKafkaTemplate.sendDefault(avroPurchaseCount.getProductId(),avroPurchaseCount);
//        }
//    }

}
