package org.mddarr.ridereceiver;

import org.apache.kafka.streams.kstream.*;

import org.mddarr.orders.event.dto.AvroOrder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import java.util.function.Consumer;

@SpringBootApplication
@EnableKafkaStreams
@EnableKafka
public class OrdersProcessingApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdersProcessingApplication.class, args);
	}

	@Bean
	public Consumer<KStream<String, AvroOrder>> process_orders() {
		return (rideRequestKStream -> {
			rideRequestKStream.foreach((k,v)->{
				System.out.println("THE INCOMING ORDER LOOKS LIKE " + v.getVendors());
			});
		});
	}

}
