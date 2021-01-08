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
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
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
@RequiredArgsConstructor
class Producer {
	private final KafkaTemplate kafkaTemplate;
	@EventListener(ApplicationStartedEvent.class)
	public void produce(){

	}
}
