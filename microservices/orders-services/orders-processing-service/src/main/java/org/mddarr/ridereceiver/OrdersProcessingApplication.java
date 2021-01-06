package org.mddarr.ridereceiver;

import org.apache.kafka.streams.kstream.*;

import org.mddarr.orders.event.dto.AvroOrder;
import org.mddarr.orders.event.dto.FirstOrder;
import org.mddarr.products.AvroProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.BlockingQueue;
import java.util.function.Function;
import java.util.function.Supplier;

@SpringBootApplication
public class OrdersProcessingApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdersProcessingApplication.class, args);
	}

	public static final String INPUT_TOPIC = "input";
	public static final String OUTPUT_TOPIC = "output";
	public static final int WINDOW_SIZE_MS = 30_000;


	@Autowired
	BlockingQueue<FirstOrder> orderBlockingQueue;
//    BlockingQueue<PersonEvent> unbounded = new LinkedBlockingQueue<>();

	@Bean
	public Supplier<FirstOrder> supplier() {
		return () -> {
			try {
				orderBlockingQueue.poll();
				FirstOrder firstOrder = orderBlockingQueue.take();
				System.out.println("Writing Order for customer '" + firstOrder.getCustomerId() + "' to input topic " + Constants.ORDERS_TOPIC);
				return firstOrder;
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("NOTHING");
				return null;
			}

		};
	}

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
