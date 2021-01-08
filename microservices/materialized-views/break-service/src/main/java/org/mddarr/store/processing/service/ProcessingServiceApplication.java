package org.mddarr.store.processing.service;

import org.apache.kafka.streams.kstream.*;

import org.mddarr.orders.event.dto.AvroOrder;
import org.mddarr.products.AvroInventory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;

@SpringBootApplication
public class ProcessingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProcessingServiceApplication.class, args);
	}

	@Bean
	public Consumer<KStream<String, AvroInventory>> process_inventory() {
		return (rideRequestKStream -> {
			rideRequestKStream.foreach((k,v)->{
				System.out.println("THE INCOMING ORDER LOOKS LIKE " + v.getProductid());
			});
		});
	}

//	@Bean
//	public Consumer<KTable<String, AvroInventory>> process_inventory() {
//		return (avroInventoryKTable -> {
//
//		});
//	}


}
