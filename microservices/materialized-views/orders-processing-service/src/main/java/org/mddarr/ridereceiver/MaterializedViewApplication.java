package org.mddarr.ridereceiver;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.mddarr.orders.event.dto.FirstOrder;
import org.mddarr.products.AvroProduct;
import org.mddarr.products.AvroPurchaseCount;
import org.mddarr.products.AvroPurchaseEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.BlockingQueue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@SpringBootApplication
public class MaterializedViewApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaterializedViewApplication.class, args);
	}

	@Bean
	public Consumer<KTable<String, AvroProduct>> process_products() {
		return(stringAvroProductKTable -> {
			System.out.println("HEAEEEEY AAA");
		});
	}


}
