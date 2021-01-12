package org.mddarr.store.processing.service;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.*;

import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.mddarr.orders.event.dto.AvroOrder;
import org.mddarr.products.AvroInventory;
import org.mddarr.products.AvroProduct;
import org.mddarr.products.AvroProductID;
import org.mddarr.products.AvroPurchaseEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

@SpringBootApplication
public class ProcessingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProcessingServiceApplication.class, args);
	}

	@Bean
	public Consumer<KStream<String, AvroPurchaseEvent>> process_purchase_events() {
		return (purchaseEventKStream -> {

			purchaseEventKStream.foreach((k,v)->{
				System.out.println("THE PROUDCT IS " + v.getProductid() + " and the key is " + k);
			});

			final Map<String, String> serdeConfig = Collections.singletonMap(
					AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

			final SpecificAvroSerde<AvroPurchaseEvent> avroInventorySerde = new SpecificAvroSerde<>();
			avroInventorySerde.configure(serdeConfig, false);

			final SpecificAvroSerde<AvroProductID> avroProductSerde = new SpecificAvroSerde<>();
			avroInventorySerde.configure(serdeConfig, false);

			KGroupedStream<String, AvroPurchaseEvent> groupedByProductID = purchaseEventKStream.groupBy((key, value)->key, Grouped.with(Serdes.String(),avroInventorySerde));

			purchaseEventKStream.foreach((k,v)->{
				System.out.println("THE PROUDCT2 IS " + v.getProductid());
			});

			final KTable<String, Long> productPurchaseCountsTable = groupedByProductID.count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as(Constants.PURCHASE_COUNT_STORE)
					.withKeySerde(Serdes.String())
					.withValueSerde(Serdes.Long()));

			productPurchaseCountsTable.toStream().foreach((k,v) -> {
				System.out.println("THE PURCHASE COUNT TABLE LOOIKS LIKE " + k + " and the number of purchases is " + v);
			} );

		});

	}


}
