package org.mddarr.materializedview;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.mddarr.materializedview.templates.KafkaGenericTemplate;
import org.mddarr.products.AvroPurchaseCount;
import org.mddarr.products.AvroPurchaseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

import static org.apache.kafka.streams.StoreQueryParameters.fromNameAndType;
import static org.apache.kafka.streams.state.QueryableStoreTypes.keyValueStore;

@SpringBootApplication
public class MaterializedViewApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaterializedViewApplication.class, args);
	}

}


@Component
@RequiredArgsConstructor
class Producer {

	private final KafkaTemplate<Integer, String> kafkaTemplate;


	@EventListener(ApplicationStartedEvent.class)
	public void produce() {
		KafkaGenericTemplate<AvroPurchaseCount> kafkaGenericTemplate = new KafkaGenericTemplate<>();
		KafkaTemplate<String, AvroPurchaseCount> kafkaPurchaseCountTemplate = kafkaGenericTemplate.getKafkaTemplate();
		kafkaPurchaseCountTemplate.setDefaultTopic(Constants.PURCHASE_COUNTS_TOPIC);
		AvroPurchaseCount avroPurchaseCount = AvroPurchaseCount.newBuilder()
				.setProductId("product1")
				.setCount(6)
				.build();

		kafkaPurchaseCountTemplate.sendDefault(avroPurchaseCount);

		kafkaTemplate.send("orders", 1, "iPad");
		kafkaTemplate.send("orders", 2, "iPhone");
		kafkaTemplate.send("orders", 1, "iPad, Airpods");
		kafkaTemplate.send("orders", 2, "HomePod");
	}
}


//@RestController
//@RequiredArgsConstructor
//class MyIqController {
//
//	private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;
//
//	@GetMapping("/iq/{id}")
//	public String getOrder(@PathVariable final Integer id) {
//		final KafkaStreams kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
//		final ReadOnlyKeyValueStore<Integer, String> store =
//				kafkaStreams.store(fromNameAndType("orders-store", keyValueStore()));
//		return store.get(id);
//	}
//}
