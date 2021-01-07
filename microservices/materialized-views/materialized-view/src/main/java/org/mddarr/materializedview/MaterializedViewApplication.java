package org.mddarr.materializedview;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class MaterializedViewApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaterializedViewApplication.class, args);
	}

}

@Component
class PurchasesView {
	public void buildOrdersView(StreamsBuilder builder){
		builder.table("purchases", Consumed.with(Serdes.Integer(), Serdes.String()), Materialized.as("purchase-store"));

	}
}

@Component
@RequiredArgsConstructor
class Producer {
	private final KafkaTemplate kafkaTemplate;
	@EventListener(ApplicationStartedEvent.class)
	public void produce(){

	}
}
