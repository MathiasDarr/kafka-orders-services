package org.mddarr.ordersviews;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mddarr.products.AvroInventory;
import org.mddarr.products.AvroProduct;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.apache.kafka.streams.StreamsConfig.*;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class InventoryTopologyTests {

    TopologyTestDriver testDriver;

    final static Map<String, String> testConfig = Map.of(
            BOOTSTRAP_SERVERS_CONFIG, "localhost:8080",
            APPLICATION_ID_CONFIG, "topology-test",
            DEFAULT_KEY_SERDE_CLASS_CONFIG, "org.apache.kafka.common.serialization.Serdes$StringSerde",
            DEFAULT_VALUE_SERDE_CLASS_CONFIG, "io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde"
    );

    private Properties properties;
    private Topology topology;


    AvroInventory inventory1;
    AvroInventory inventory2;


    @BeforeEach
    public void SetUp(){
        properties = new Properties();
        properties.putAll(testConfig);

        final StreamsBuilder streamsBuilder = new StreamsBuilder();

        topology = new InventoryTopology()
                .topology(streamsBuilder);

        inventory1 = AvroInventory.newBuilder()
                .setProductid("product1")
                .setInventory(120)
                .build();

        inventory2 = AvroInventory.newBuilder()
                .setProductid("product2")
                .setInventory(60)
                .build();
    }

    @Test
    public void testDriverShouldNotBeNull() {
        final StreamsBuilder streamsBuilder = new StreamsBuilder();

        try (TopologyTestDriver testDriver = new TopologyTestDriver(topology, properties)) {
            assertThat(testDriver, not(nullValue())
            );
        }
    }


    @Test
    public void shouldCreateSuccessfulTransaction() {

        try (TopologyTestDriver testDriver = new TopologyTestDriver(topology, properties)) {


        }
    }


    @Test
    void shouldCreateTopology() {

        final StreamsBuilder streamsBuilder = new StreamsBuilder();
        topology = new InventoryTopology()
                .topology(streamsBuilder);

        Properties config = new Properties();
        config.putAll(Map.of(APPLICATION_ID_CONFIG, "test-app",
                BOOTSTRAP_SERVERS_CONFIG, "dummy:9092"));

        final Map<String, String> serdeConfig = Collections.singletonMap(
                AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        // Set serializers and
        final SpecificAvroSerializer<AvroInventory> productInventorySerializer = new SpecificAvroSerializer<>();
        productInventorySerializer.configure(serdeConfig, false);

        try (TopologyTestDriver topologyTestDriver = new TopologyTestDriver(streamsBuilder.build(), config)) {
            final TestInputTopic<String, AvroInventory> inventory_topic = topologyTestDriver.createInputTopic(Constants.PRODUCT_INVENTORY_TOPIC,
                            Serdes.String().serializer(),
                            productInventorySerializer);
            inventory_topic.pipeInput("product1",inventory1);
            inventory_topic.pipeInput("product2",inventory2);



        }



    }

}
