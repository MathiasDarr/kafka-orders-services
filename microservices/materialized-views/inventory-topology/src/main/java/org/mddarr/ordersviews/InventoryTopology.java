package org.mddarr.ordersviews;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.Stores;
import org.mddarr.orders.event.dto.AvroOrder;
import org.mddarr.orders.event.dto.AvroOrderResult;
import org.mddarr.ordersviews.Constants;
import org.mddarr.products.AvroInventory;
import org.mddarr.products.AvroPurchaseCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

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

        KTable<String, AvroInventory> avroInventoryTable = streamsBuilder.table(Constants.PRODUCT_INVENTORY_TOPIC,
                Consumed.with(Serdes.String(), avroInventorySerde),Materialized.as(Constants.PRODUCT_INVENTORY_STORE));

            /*
            This line of code does several things
            1) subscirbes to events on this topic
            2) Resets to the earliest offset & loads all events into the Kafka Streams API
            3) Pushes these events into a state store, a local, disk resident hash table locationed in the Kafka Streams API.
             */

        KStream<String, AvroOrder> avroOrderKStream =
                streamsBuilder.stream(Constants.ORDERS_TOPIC);

//        KStream<String, AvroOrderResult> avroOrderResultStream = avroOrderKStream
//                .transformValues(OrderTransformer::new, Constants.PRODUCT_INVENTORY_STORE);

        avroOrderKStream.to(Constants.ORDERS_VALIDATION_TOPIC);


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
