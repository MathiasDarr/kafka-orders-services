package org.mddarr.ordersviews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@SpringBootApplication
@EnableKafka
@EnableKafkaStreams
public class InventoryTopologyApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryTopologyApplication.class, args);
	}

}



