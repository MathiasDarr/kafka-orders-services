package org.mddarr.rideservice;


import org.mddarr.rideservice.mock.CustomKafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mddarr.rides.event.dto.AvroRideRequest;
import org.mddarr.rides.event.dto.Event3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(classes = {ShowcaseApp.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@Category(IntegrationTest.class)
@EmbeddedKafka()
public abstract class UatAbstractTest {
    @Autowired
    private KafkaProperties kafkaProperties;
    @Autowired
    private EmbeddedKafkaBroker kafkaEmbedded;
    protected Producer<String, Event3> event3Producer;
    protected Consumer<String, Event3> event3Consumer;
    protected Consumer<String, AvroRideRequest> event1Consumer;




    @Before
    public void setUp() {
        Map<String, Object> senderProps = kafkaProperties.buildProducerProperties();

        event3Producer = new KafkaProducer<>(senderProps);

        //consumers used in test code needs to be created like this in code because otherwise it won't work
        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.consumerProps("in-test-consumer", "false", kafkaEmbedded));
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CustomKafkaAvroDeserializer.class);
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        configs.put("schema.registry.url", "not-used");

        event3Consumer = new DefaultKafkaConsumerFactory<String, Event3>(configs).createConsumer("in-test-consumer", "10");

        event1Consumer = new DefaultKafkaConsumerFactory<String, AvroRideRequest>(configs).createConsumer("in-test-consumer", "10");

        kafkaProperties.buildConsumerProperties();
        event3Consumer.subscribe(Lists.newArrayList(Constants.EVENT_3_TOPIC));

        event1Consumer.subscribe(Lists.newArrayList(Constants.Rides_TOPIC));

    }

    @After
    public void reset() {
        //consumers needs to be closed because new one are created before every test
        event3Consumer.close();

        event1Consumer.close();

    }

}
