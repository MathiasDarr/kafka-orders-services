package org.mddarr.store.inventory.service;

import org.apache.kafka.streams.kstream.*;

import org.mddarr.orders.event.dto.AvroOrder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
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
