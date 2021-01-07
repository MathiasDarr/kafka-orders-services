package org.mddarr.materializedview;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.mddarr.products.AvroPurchaseCount;
import org.mddarr.products.AvroPurchaseEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@SpringBootApplication
public class MaterializedViewApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaterializedViewApplication.class, args);
	}

}

@Component
class PurchasesView {
	public void buildPurchaseEventView(StreamsBuilder builder){
		final Map<String, String> serdeConfig = Collections.singletonMap(
				AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
		final SpecificAvroSerde<AvroPurchaseEvent> purchaseEventSerde = new SpecificAvroSerde<>();
		purchaseEventSerde.configure(serdeConfig, false);
		builder.table(Constants.PURCHASE_EVENTS_TOPIC, Consumed.with(Serdes.String(),purchaseEventSerde), Materialized.as(Constants.PURCHASE_EVENTS_STORE));
	}
}

@Component
class PurchaseCountView {
	public void buildPurchaseCountView(StreamsBuilder builder){
		final Map<String, String> serdeConfig = Collections.singletonMap(
				AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

		final SpecificAvroSerde<AvroPurchaseCount> purchaseCountSerde = new SpecificAvroSerde<>();
		purchaseCountSerde.configure(serdeConfig, false);
		builder.table(Constants.PURCHASE_COUNTS_TOPIC, Consumed.with(Serdes.String(),purchaseCountSerde), Materialized.as(Constants.PURCHASE_COUNTS_STORE));
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
