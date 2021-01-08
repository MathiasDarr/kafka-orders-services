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
import org.mddarr.materializedview.views.PurchaseCountView;
import org.mddarr.materializedview.views.PurchasesView;
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
				.setProductid("produc1")
				.build();

		AvroPurchaseEvent avroPurchaseEvent2 = AvroPurchaseEvent.newBuilder()
				.setProductid("product2")
				.build();

		AvroPurchaseEvent avroPurchaseEvent3 = AvroPurchaseEvent.newBuilder()
				.setProductid("product3")
				.build();

		AvroPurchaseEvent avroPurchaseEvent4 = AvroPurchaseEvent.newBuilder()
				.setProductid("product4")
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
