package org.mddarr.ordersviews;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.processor.MockProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.Stores;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mddarr.orders.event.dto.AvroOrder;
import org.mddarr.orders.event.dto.AvroOrderResult;
import org.mddarr.orders.event.dto.OrderState;
import org.mddarr.products.AvroInventory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.math.BigDecimal;
import java.util.*;

import static org.apache.kafka.streams.StreamsConfig.*;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class OrdersTransformerTest {

    TopologyTestDriver testDriver;
    private MockProcessorContext mockContext;
    private KeyValueStore<String, AvroInventory> avroInventoryKeyValueStore;

    private OrderValidationTransformer orderValidationTransformer;
    private String inventory_store_name = Constants.PRODUCT_INVENTORY_STORE;

    final static Map<String, String> testConfig = Map.of(
            BOOTSTRAP_SERVERS_CONFIG, "localhost:8080",
            APPLICATION_ID_CONFIG, "orders-transformer-test",
            DEFAULT_KEY_SERDE_CLASS_CONFIG, "org.apache.kafka.common.serialization.Serdes$StringSerde",
            DEFAULT_VALUE_SERDE_CLASS_CONFIG, "io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde"
    );

    @BeforeEach
    public void setup() {
        final Properties properties = new Properties();
        properties.putAll(testConfig);
        mockContext = new MockProcessorContext(properties);


        final Map<String, String> serdeConfig = Collections.singletonMap(
                AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

        final SpecificAvroSerde<AvroInventory> avroInventorySerde = new SpecificAvroSerde<>();

        avroInventorySerde.configure(serdeConfig, false);

        avroInventoryKeyValueStore = Stores.keyValueStoreBuilder(
                Stores.inMemoryKeyValueStore(inventory_store_name),
                Serdes.String(),
                avroInventorySerde)
                .withLoggingDisabled()    // Changelog is not supported by MockProcessorContext.
                .build();

        avroInventoryKeyValueStore.init(mockContext, avroInventoryKeyValueStore);
        mockContext.register(avroInventoryKeyValueStore, null);

        orderValidationTransformer = new OrderValidationTransformer(inventory_store_name);

        orderValidationTransformer.init(mockContext);
    }


    @Test
    public void transformedOrderShouldBeSuccessfull() {
        final AvroInventory inventory = new AvroInventory("product1", 2L);

        List<String> products = new ArrayList<>(Arrays.asList("item1"));
        List<String> vendors = new ArrayList<>(Arrays.asList("vendor1"));
        List<Long> quantities = new ArrayList<>(Arrays.asList(1L));
        List<String> productids = new ArrayList<>(Arrays.asList("product1"));

        AvroOrder order1 = AvroOrder.newBuilder()
                .setCustomerId("jerr@gmail.com")
                .setId("order1")
                .setProducts(products)
                .setProductids(productids)
                .setQuantites(quantities)
                .setVendors(vendors)
                .setPrice(120.0)
                .setState(OrderState.PENDING)
                .build();

        final AvroOrderResult orderResult = orderValidationTransformer.transform(order1);
        Assertions.assertEquals(orderResult.getId(), order1.getId());
        Assertions.assertEquals(orderResult.getResult(), true);
    }


}
