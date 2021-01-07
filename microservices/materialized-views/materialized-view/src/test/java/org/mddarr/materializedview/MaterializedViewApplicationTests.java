package org.mddarr.materializedview;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.state.KeyValueStore;
import org.assertj.core.api.Assertions;
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
	void shouldCreatePurchaseCountMaterializedView(){
		/*
		In this test we verify the state store is storing the most recently updated record
		 */
		final StreamsBuilder builder = new StreamsBuilder();

		new PurchaseCountView().buildPurchaseCountView(builder);
		Properties config = new Properties();
		config.putAll(Map.of(StreamsConfig.APPLICATION_ID_CONFIG, "test-app-2", StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092"));
		final TopologyTestDriver topologyTestDriver = new TopologyTestDriver(builder.build(), config);

		// Configure the SpecificAvroSerdes
		final Map<String, String> serdeConfig = Collections.singletonMap(
				AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

		final SpecificAvroSerde<AvroPurchaseCount> purchaseCountSerde = new SpecificAvroSerde<>();
		purchaseCountSerde.configure(serdeConfig, false);

		topologyTestDriver.createInputTopic(Constants.PURCHASE_COUNTS_TOPIC, Serdes.String().serializer(), purchaseCountSerde.serializer());

		final TestInputTopic<String, AvroPurchaseCount> purchaseCounts = topologyTestDriver.createInputTopic(Constants.PURCHASE_COUNTS_TOPIC, Serdes.String().serializer(),  purchaseCountSerde.serializer());

		AvroPurchaseCount avroPurchaseCount = AvroPurchaseCount.newBuilder()
				.setProductId("product1")
				.setCount(129)
				.build();

		AvroPurchaseCount avroPurchaseCount2 = AvroPurchaseCount.newBuilder()
				.setProductId("product2")
				.setCount(129)
				.build();

		AvroPurchaseCount avroPurchaseCount3 = AvroPurchaseCount.newBuilder()
				.setProductId("product1")
				.setCount(150)
				.build();

		AvroPurchaseCount avroPurchaseCount4 = AvroPurchaseCount.newBuilder()
				.setProductId("product3")
				.setCount(5)
				.build();

		purchaseCounts.pipeInput("a",avroPurchaseCount);
		purchaseCounts.pipeInput("b",avroPurchaseCount2);
		purchaseCounts.pipeInput("a",avroPurchaseCount3);
		purchaseCounts.pipeInput("b",avroPurchaseCount4);

		final KeyValueStore<String, AvroPurchaseCount> keyValueStore = topologyTestDriver.getKeyValueStore(Constants.PURCHASE_COUNTS_STORE);

		AvroPurchaseCount purchaseCount = keyValueStore.get("a");

		Assertions.assertThat(purchaseCount.getCount()).isEqualTo(150);

	}


	@Test
	void shouldCreateMaterializedView(){
		/*
		In this test we verify the state store is storing the most recently updated record
		 */

		final StreamsBuilder builder = new StreamsBuilder();

		new PurchasesView().buildPurchaseEventView(builder);
		Properties config = new Properties();
		config.putAll(Map.of(StreamsConfig.APPLICATION_ID_CONFIG, "test-app", StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092"));
		final TopologyTestDriver topologyTestDriver = new TopologyTestDriver(builder.build(), config);

		// Configure the SpecificAvroSerdes
		final Map<String, String> serdeConfig = Collections.singletonMap(
				AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

		final SpecificAvroSerde<AvroPurchaseEvent> purchaseEventSerde = new SpecificAvroSerde<>();
		purchaseEventSerde.configure(serdeConfig, false);

		topologyTestDriver.createInputTopic(Constants.PURCHASE_EVENTS_TOPIC, Serdes.String().serializer(), purchaseEventSerde.serializer());

		final TestInputTopic<String, AvroPurchaseEvent> puchasesEvents = topologyTestDriver.createInputTopic(Constants.PURCHASE_EVENTS_TOPIC, Serdes.String().serializer(),  purchaseEventSerde.serializer());

		AvroPurchaseEvent avroPurchaseEvent = AvroPurchaseEvent.newBuilder()
				.setProduct("produc1")
				.setVendor("osprey")
				.build();

		AvroPurchaseEvent avroPurchaseEvent2 = AvroPurchaseEvent.newBuilder()
				.setProduct("product2")
				.setVendor("north face")
				.build();

		AvroPurchaseEvent avroPurchaseEvent3 = AvroPurchaseEvent.newBuilder()
				.setProduct("product3")
				.setVendor("osprey")
				.build();

		AvroPurchaseEvent avroPurchaseEvent4 = AvroPurchaseEvent.newBuilder()
				.setProduct("product4")
				.setVendor("dakobed")
				.build();

		puchasesEvents.pipeInput("a",avroPurchaseEvent);
		puchasesEvents.pipeInput("b",avroPurchaseEvent2);
		puchasesEvents.pipeInput("a",avroPurchaseEvent3);
		puchasesEvents.pipeInput("b",avroPurchaseEvent4);

		final KeyValueStore<String, AvroPurchaseEvent> keyValueStore = topologyTestDriver.getKeyValueStore(Constants.PURCHASE_EVENTS_STORE);

		Assertions.assertThat(keyValueStore.get("a")).isEqualTo(avroPurchaseEvent3);
		Assertions.assertThat(keyValueStore.get("b")).isEqualTo(avroPurchaseEvent4);

	}

}