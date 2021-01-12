package org.mddarr.ordersviews;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mddarr.orders.event.dto.AvroOrder;
import org.mddarr.orders.event.dto.OrderState;
import org.mddarr.products.AvroInventory;

import java.util.*;

import static org.apache.kafka.streams.StreamsConfig.*;
import static org.apache.kafka.streams.StreamsConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class OrdersValidationTests {
    TopologyTestDriver testDriver;

    final static Map<String, String> testConfig = Map.of(
            BOOTSTRAP_SERVERS_CONFIG, "localhost:8080",
            APPLICATION_ID_CONFIG, "topology-test",
            DEFAULT_KEY_SERDE_CLASS_CONFIG, "org.apache.kafka.common.serialization.Serdes$StringSerde",
            DEFAULT_VALUE_SERDE_CLASS_CONFIG, "io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde"
    );

    private Properties properties;
    private Topology topology;


    @BeforeEach
    public void SetUp(){
        properties = new Properties();
        properties.putAll(testConfig);

        final StreamsBuilder streamsBuilder = new StreamsBuilder();

        topology = new InventoryTopology()
                .topology(streamsBuilder);



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
    void shouldOutputValidatedOrder() {

        final StreamsBuilder streamsBuilder = new StreamsBuilder();
        topology = new InventoryTopology()
                .topology(streamsBuilder);

        Properties config = new Properties();
        config.putAll(Map.of(APPLICATION_ID_CONFIG, "test-app",
                BOOTSTRAP_SERVERS_CONFIG, "dummy:9092"));

        final Map<String, String> serdeConfig = Collections.singletonMap(
                AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        // Set serializers and
        final SpecificAvroSerializer<AvroOrder> productInventorySerializer = new SpecificAvroSerializer<>();
        productInventorySerializer.configure(serdeConfig, false);

        try (TopologyTestDriver topologyTestDriver = new TopologyTestDriver(streamsBuilder.build(), config)) {
            final TestInputTopic<String, AvroOrder> orders_topic = topologyTestDriver.createInputTopic(Constants.ORDERS_TOPIC,
                    Serdes.String().serializer(),
                    productInventorySerializer);

            List<String> products = new ArrayList<>(Arrays.asList("item1"));
            List<String> vendors = new ArrayList<>(Arrays.asList("vendor1"));
            List<Long> quantities = new ArrayList<>(Arrays.asList(1L));

            AvroOrder order1 = AvroOrder.newBuilder()
                    .setCustomerId("jerr@gmail.com")
                    .setId("order1")
                    .setProducts(products)
                    .setQuantites(quantities)
                    .setVendors(vendors)
                    .setPrice(120.0)
                    .setState(OrderState.PENDING)
                    .build();

            orders_topic.pipeInput(order1.getId(),order1);

        }



    }


}
