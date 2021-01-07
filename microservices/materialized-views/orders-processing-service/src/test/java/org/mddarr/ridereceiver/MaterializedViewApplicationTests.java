package org.mddarr.ridereceiver;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.state.KeyValueStore;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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

		new OrderView().buildOrdersView(builder);
		Properties config = new Properties();
		config.putAll(Map.of(StreamsConfig.APPLICATION_ID_CONFIG, "test-app", StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092"));
		final TopologyTestDriver topologyTestDriver = new TopologyTestDriver(builder.build(), config);

		topologyTestDriver.createInputTopic("orders", Serdes.Integer().serializer(), Serdes.String().serializer());

		final TestInputTopic<Integer, String> orders = topologyTestDriver.createInputTopic("orders", Serdes.Integer().serializer(), Serdes.String().serializer());
		orders.pipeInput(1,"iPhone");
		orders.pipeInput(2,"iPad");
		orders.pipeInput(1,"iPad,AirPods");
		orders.pipeInput(2,"HomePod");

		final KeyValueStore<Integer, String> keyValueStore = topologyTestDriver.getKeyValueStore("orders-store");


	}

}
