package org.mddarr.inventory.service;


import org.mddarr.orders.event.dto.AvroOrder;
import org.mddarr.inventory.service.mock.CustomKafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;


import org.mddarr.products.AvroProduct;
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

@SpringBootTest(classes = {InventoryServiceApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@Category(IntegrationTest.class)
@EmbeddedKafka()
public abstract class UatAbstractTest {
    @Autowired
    private KafkaProperties kafkaProperties;
    @Autowired
    private EmbeddedKafkaBroker kafkaEmbedded;
    protected Producer<String, AvroProduct> orderProducer;
    protected Consumer<String, AvroProduct> ordersConsumer;




    @Before
    public void setUp() {
        Map<String, Object> senderProps = kafkaProperties.buildProducerProperties();

        orderProducer = new KafkaProducer<>(senderProps);

        //consumers used in test code needs to be created like this in code because otherwise it won't work
        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.consumerProps("in-test-consumer", "false", kafkaEmbedded));
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CustomKafkaAvroDeserializer.class);
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        configs.put("schema.registry.url", "not-used");


        ordersConsumer = new DefaultKafkaConsumerFactory<String, AvroProduct>(configs).createConsumer("in-test-consumer", "10");

        kafkaProperties.buildConsumerProperties();


        ordersConsumer.subscribe(Lists.newArrayList(Constants.INVENTORY));

    }

    @After
    public void reset() {
        //consumers needs to be closed because new one are created before every test
        ordersConsumer.close();

    }

}
