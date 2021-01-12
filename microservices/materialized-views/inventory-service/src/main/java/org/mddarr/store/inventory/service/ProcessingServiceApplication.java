package org.mddarr.store.inventory.service;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.*;

import org.apache.kafka.streams.state.KeyValueStore;
import org.mddarr.orders.event.dto.AvroOrder;
import org.mddarr.products.AvroProductID;
import org.mddarr.products.AvroPurchaseEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.awt.datatransfer.SystemFlavorMap;
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

@SpringBootApplication
public class ProcessingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProcessingServiceApplication.class, args);
	}

	@Bean
	public Consumer<KStream<String, AvroOrder>> process_orders() {
		return (ordersKStream -> {
			ordersKStream.foreach((k,v)->{
				System.out.println("ADDF " + k + " dfdae " + v);
			});

		});

	}


}
