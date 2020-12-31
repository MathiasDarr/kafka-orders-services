package org.mddarr.ridereceiver;

import org.apache.kafka.streams.kstream.*;

import org.mddarr.orders.event.dto.AvroOrder;
import org.mddarr.products.AvroProduct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

@SpringBootApplication
public class OrdersProcessingApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdersProcessingApplication.class, args);
	}

	public static final String INPUT_TOPIC = "input";
	public static final String OUTPUT_TOPIC = "output";
	public static final int WINDOW_SIZE_MS = 30_000;


	@Bean
	public Function<KStream<String, AvroOrder>, KStream<String, AvroOrder>>  process_orders() {
		return (avroOrderKStream) -> {
			avroOrderKStream.foreach((key, value) -> System.out.println("THE KEY IS AND THE VLAUE IS " + key + " " + value));
			return avroOrderKStream;

		};
	}

	@Bean
	public Function<KStream<String, AvroProduct>, KStream<String, AvroProduct>>  process_inventory() {
		return (avroInventoryKStream) -> {
			avroInventoryKStream.foreach((key, value) -> System.out.println("THE KEY IS AND THE VLAUE IS " + key + " " + value));
			return avroInventoryKStream;

		};
	}



}
