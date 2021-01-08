package org.mddarr.ordersviews.producers;

import lombok.RequiredArgsConstructor;
import org.mddarr.ordersviews.Constants;
import org.mddarr.ordersviews.templates.KafkaGenericTemplate;
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
    class ProductInventoryProducer {

        private final KafkaTemplate<Integer, String> kafkaTemplate;
        @EventListener(ApplicationStartedEvent.class)
        public void produce() {
            kafkaTemplate.send(Constants.PRODUCT_INVENTORY_TOPIC_STRING, 1, "iPad");
            kafkaTemplate.send(Constants.PRODUCT_INVENTORY_TOPIC_STRING, 2, "iPhone");
            kafkaTemplate.send(Constants.PRODUCT_INVENTORY_TOPIC_STRING, 1, "iPad, Airpods");
            kafkaTemplate.send(Constants.PRODUCT_INVENTORY_TOPIC_STRING, 2, "HomePod");
        }
    }

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
//            purchaseEventKafkaTemplate.setDefaultTopic(Constants.PURCHASE_EVENT_TOPIC);
//
//            AvroPurchaseCount avroPurchaseCount = AvroPurchaseCount.newBuilder()
//                    .setProductId("product1")
//                    .setCount(120)
//                    .build();
//
//            purchaseEventKafkaTemplate.sendDefault(avroPurchaseCount);
//        }
//    }
//
//


    @Component
    @RequiredArgsConstructor
    class PurchaseCountProducer {

        private final KafkaTemplate<Integer, String> kafkaTemplate;

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

}