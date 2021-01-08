package org.mddarr.materializedview.views;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.mddarr.materializedview.Constants;
import org.mddarr.products.AvroPurchaseCount;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class PurchaseCountView {
    public void buildPurchaseCountView(StreamsBuilder builder) {
        final Map<String, String> serdeConfig = Collections.singletonMap(
                AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

        final SpecificAvroSerde<AvroPurchaseCount> purchaseCountSerde = new SpecificAvroSerde<>();
        purchaseCountSerde.configure(serdeConfig, false);
        builder.table(Constants.PURCHASE_COUNTS_TOPIC, Consumed.with(Serdes.String(), purchaseCountSerde), Materialized.as(Constants.PURCHASE_COUNTS_STORE));
    }
}
