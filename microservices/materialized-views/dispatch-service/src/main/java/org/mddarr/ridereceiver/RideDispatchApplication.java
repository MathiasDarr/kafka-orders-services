package org.mddarr.ridereceiver;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.*;

import org.mddarr.orders.event.dto.AvroOrder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;
import java.util.function.Function;

@SpringBootApplication
public class RideDispatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(RideDispatchApplication.class, args);
	}

	@Bean
	public Consumer<KStream<String, AvroOrder>> process_orders() {
		return (rideRequestKStream -> {
			rideRequestKStream.foreach((k,v)->{
				System.out.println("THE INCOMING ORDER LOOKS LIKE " + v.getVendors());
			});
		});

	}


//	@Bean
//	public Function<KStream<String, AvroRideRequest>, KStream<String, AvroRide>>  process_ride_requests() {
//		return (rideRequestKStream -> {
//			KStream<String, AvroRide> rideKStream = rideRequestKStream
//					.map((key,avroRideRequest)->new KeyValue<>(key, new AvroRide("ride1",avroRideRequest.getUserId(), "Bathsheba")));
//			return rideKStream;
//		});
//
//	}


}
