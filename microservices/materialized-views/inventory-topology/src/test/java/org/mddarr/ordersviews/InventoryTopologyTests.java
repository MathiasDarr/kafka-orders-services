package org.mddarr.ordersviews;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;
import java.util.Properties;

import static org.apache.kafka.streams.StreamsConfig.*;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG;

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

    @Test
    void shouldCreateTopology() {

        final StreamsBuilder streamsBuilder = new StreamsBuilder();
        topology = new KStreamConfig()
                .topology(streamsBuilder);

    }


}
