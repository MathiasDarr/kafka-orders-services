package org.mddarr.materializedview;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.state.KeyValueStore;
import org.junit.jupiter.api.Test;
import org.mddarr.products.AvroPurchaseCount;
import org.mddarr.products.AvroPurchaseEvent;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

@SpringBootTest
class MaterializedViewApplicationTests {

	@Test
	void contextLoads() {
	}


	@Test
	void shouldCreateMaterializedView(){
//		final TopologyTestDriver topologyTestDriver = new TopologyTestDriver();
		final StreamsBuilder builder = new StreamsBuilder();

		new PurchasesView().buildOrdersView(builder);
		Properties config = new Properties();
		config.putAll(Map.of(StreamsConfig.APPLICATION_ID_CONFIG, "test-app", StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092"));
		final TopologyTestDriver topologyTestDriver = new TopologyTestDriver(builder.build(), config);

		// Configure the SpecificAvroSerdes
		final Map<String, String> serdeConfig = Collections.singletonMap(
				AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

		final SpecificAvroSerde<AvroPurchaseEvent> purchaseEventSerde = new SpecificAvroSerde<>();
		purchaseEventSerde.configure(serdeConfig, false);

		topologyTestDriver.createInputTopic("puchases", Serdes.String().serializer(), purchaseEventSerde.serializer());

		final TestInputTopic<String, AvroPurchaseEvent> puchasesEvents = topologyTestDriver.createInputTopic("purchases", Serdes.String().serializer(),  purchaseEventSerde.serializer());

		AvroPurchaseEvent avroPurchaseEvent = AvroPurchaseEvent.newBuilder()
				.setProduct("produc1")
				.setVendor("osprey")
				.build();
		puchasesEvents.pipeInput(avroPurchaseEvent);
//		orders.pipeInput(1,"iPhone");
//		orders.pipeInput(2,"iPad");
//		orders.pipeInput(1,"iPad,AirPods");
//		orders.pipeInput(2,"HomePod");

		final KeyValueStore<Integer, String> keyValueStore = topologyTestDriver.getKeyValueStore("orders-store");

	}

}
