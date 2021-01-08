package org.mddarr.ordersviews.views;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.mddarr.ordersviews.Constants;
import org.mddarr.products.AvroPurchaseCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Configuration
public class Views {

//    @Component
//    public static class InventoryView {
//
//        @Autowired
//        public void buildInventoryView(StreamsBuilder builder) {
//            builder.table(Constants.PRODUCT_INVENTORY_TOPIC_STRING,
//                    Consumed.with(Serdes.Integer(), Serdes.String()),
//                    Materialized.as(Constants.PRODUCT_INVENTORY_STORE));
//        }
//    }

    @Component
    public static class OrdersView {
        @Autowired
        public void buildPurchaseCountView(StreamsBuilder builder) {
//            final Map<String, String> serdeConfig = Collections.singletonMap(
//                    AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
//
//            final SpecificAvroSerde<AvroPurchaseCount> purchaseCountSerde = new SpecificAvroSerde<>();
//            purchaseCountSerde.configure(serdeConfig, false);
            builder.table(Constants.ORDERS_TOPIC, Consumed.with(Serdes.Integer(), Serdes.String()), Materialized.as(Constants.ORDERS_STORE));
        }
    }

}
