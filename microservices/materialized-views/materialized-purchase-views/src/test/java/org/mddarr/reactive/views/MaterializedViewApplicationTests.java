package org.mddarr.reactive.views;

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


}
