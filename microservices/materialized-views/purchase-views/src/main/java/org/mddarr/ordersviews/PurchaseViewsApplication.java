package org.mddarr.ordersviews;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableKafka
@EnableKafkaStreams
public class PurchaseViewsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PurchaseViewsApplication.class, args);
	}

}

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

