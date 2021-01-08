package org.mddarr.orders.processing.service.producers;

import lombok.RequiredArgsConstructor;
import org.mddarr.orders.processing.service.Constants;
import org.mddarr.orders.processing.service.templates.KafkaGenericTemplate;
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
