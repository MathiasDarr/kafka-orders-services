package org.mddarr.ordersviews;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.mddarr.orders.event.dto.AvroOrder;
import org.mddarr.orders.event.dto.AvroOrderResult;
import org.mddarr.products.AvroInventory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import java.util.*;

@Configuration
@EnableKafkaStreams
@Slf4j
@RequiredArgsConstructor
public class InventoryTopology {

    @Bean
    public Topology topology(StreamsBuilder streamsBuilder){
//        final Map<String, String> serdeConfig = Collections.singletonMap(
//                AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
//
//        final SpecificAvroSerde<AvroInventory> avroInventorySerde = new SpecificAvroSerde<>();
//        avroInventorySerde.configure(serdeConfig, false);
//
//        streamsBuilder.addStateStore(
//                Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(Constants.TOPOLOGY_STORE),
//                        Serdes.String(), avroInventorySerde));
//        defineStreams(streamsBuilder);
//
        Topology topology = streamsBuilder.build();
        defineStreams(streamsBuilder);
        return topology;
    }
//

    protected void defineStreams(StreamsBuilder streamsBuilder) {

        /*
        orders topic KStream
         */

        final Map<String, String> serdeConfig = Collections.singletonMap(
                AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

        final SpecificAvroSerde<AvroInventory> avroInventorySerde = new SpecificAvroSerde<>();

        avroInventorySerde.configure(serdeConfig, false);

        String inventoryStoreName = Constants.PRODUCT_INVENTORY_STORE;


        /*
            This line of code does several things
            1) subscirbes to events on this topic
            2) Resets to the earliest offset & loads all events into the Kafka Streams API
            3) Pushes these events into a state store, a local, disk resident hash table locationed in the Kafka Streams API.
        */
        KTable<String, AvroInventory> avroInventoryTable = streamsBuilder.table(Constants.PRODUCT_INVENTORY_TOPIC,
                Consumed.with(Serdes.String(), avroInventorySerde),
                Materialized.as(inventoryStoreName));

        final SpecificAvroSerde<AvroOrder> avroOrderSerde = new SpecificAvroSerde<>();
        avroOrderSerde.configure(serdeConfig, false);

        final SpecificAvroSerde<AvroOrderResult> avroOrderResultSerde = new SpecificAvroSerde<>();
        avroOrderResultSerde.configure(serdeConfig, false);


        KStream<String, AvroOrder> avroOrderStream = streamsBuilder.stream(Constants.ORDERS_TOPIC, Consumed.with(Serdes.String(),avroOrderSerde));

//        KStream<String, AvroOrderResult> orderResultKStream = avroOrderStream.map((k,v)->KeyValue.pair(k,AvroOrderResult.newBuilder().setId("product1").setResult(true).build()));


        KStream<String, AvroOrderResult> avroOrderResultKStream = avroOrderStream.transformValues(() -> new OrderValidationTransformer(inventoryStoreName), inventoryStoreName);
        avroOrderResultKStream.to(Constants.ORDERS_VALIDATION_TOPIC, Produced.with(Serdes.String(), avroOrderResultSerde));
//        orderResultKStream.to(Constants.ORDERS_VALIDATION_TOPIC, Produced.with(Serdes.String(), avroOrderResultSerde));

//        KStream<String, AvroOrderResult> orderResultKStream = avroOrderStream.map((k,v)->KeyValue.pair(k,v));
//        orderResultKStream.to(Constants.ORDERS_VALIDATION_TOPIC, Produced.with(Serdes.String(), avroOrderSerde));

    }

    public static AvroOrderResult verifyInventory(AvroOrder avroOrder){
            AvroOrderResult avroOrderResult = AvroOrderResult.newBuilder()
                .setId(avroOrder.getId())
                .setResult(true)
                .build();
            return avroOrderResult;

    }


//    @Component
//    public static class InventoryView {
//
//        @Autowired
//        public void buildInventoryView(StreamsBuilder builder) {
//            final Map<String, String> serdeConfig = Collections.singletonMap(
//                    AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
//
//            final SpecificAvroSerde<AvroInventory> avroInventorySerde = new SpecificAvroSerde<>();
//            avroInventorySerde.configure(serdeConfig, false);
//            /*
//            This line of code does several things
//            1) subscirbes to events on this topic
//            2) Resets to the earliest offset & loads all events into the Kafka Streams API
//            3) Pushes these events into a state store, a local, disk resident hash table locationed in the Kafka Streams API.
//             */
//
//            builder.table(Constants.PRODUCT_INVENTORY_TOPIC,
//                    Consumed.with(Serdes.String(), avroInventorySerde),
//                    Materialized.as(Constants.PRODUCT_INVENTORY_STORE));
//        }
//    }

//    @Component
//    public static class OrdersView {
//        @Autowired
//        public void buildOrdersView(StreamsBuilder builder) {
////            final Map<String, String> serdeConfig = Collections.singletonMap(
////                    AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
////
////            final SpecificAvroSerde<AvroPurchaseCount> purchaseCountSerde = new SpecificAvroSerde<>();
////            purchaseCountSerde.configure(serdeConfig, false);
//            builder.table(Constants.ORDERS_TOPIC, Consumed.with(Serdes.Integer(), Serdes.String()), Materialized.as(Constants.ORDERS_STORE));
//        }
//    }

}
