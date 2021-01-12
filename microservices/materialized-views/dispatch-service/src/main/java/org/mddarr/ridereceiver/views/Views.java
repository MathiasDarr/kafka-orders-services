package org.mddarr.ridereceiver.views;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;

import org.mddarr.products.AvroInventory;
import org.mddarr.products.AvroProduct;
import org.mddarr.products.AvroPurchaseCount;
import org.mddarr.ridereceiver.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Configuration
public class Views {

    @Component
    public static class InventoryView {
        @Autowired
        public void buildInventoryView(StreamsBuilder builder) {
            final Map<String, String> serdeConfig = Collections.singletonMap(
                    AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
            final SpecificAvroSerde<AvroProduct> purchaseCountSerde = new SpecificAvroSerde<>();
            purchaseCountSerde.configure(serdeConfig, false);
            builder.table(Constants.INVENTORY_TOPIC, Consumed.with(Serdes.String(), purchaseCountSerde), Materialized.as(Constants.INVENTORY_STORE));
        }
    }


}
