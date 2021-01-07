package org.mddarr.reactive.views.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
class Producer {

    private final KafkaTemplate<Integer, String> kafkaTemplate;

    @EventListener(ApplicationStartedEvent.class)
    public void produce() {
        kafkaTemplate.send("orders", 1, "iPad");
        kafkaTemplate.send("orders", 2, "iPhone");
        kafkaTemplate.send("orders", 1, "iPad, Airpods");
        kafkaTemplate.send("orders", 2, "HomePod");
    }
}