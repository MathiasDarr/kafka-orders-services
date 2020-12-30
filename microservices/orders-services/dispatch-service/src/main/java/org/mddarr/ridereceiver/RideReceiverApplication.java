package org.mddarr.ridereceiver;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.*;
import org.mddarr.ridereceiver.models.WordCount;
import org.mddarr.rides.event.dto.AvroDriver;
import org.mddarr.rides.event.dto.AvroRideRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@SpringBootApplication
public class RideReceiverApplication {

	public static void main(String[] args) {
		SpringApplication.run(RideReceiverApplication.class, args);
	}

	public static final String INPUT_TOPIC = "input";
	public static final String OUTPUT_TOPIC = "output";
	public static final int WINDOW_SIZE_MS = 30_000;


	@Bean
	public Function<KStream<String, AvroRideRequest>, KStream<String, AvroRideRequest>>  process() {
		return (rideRequestStream) -> {
			rideRequestStream.foreach((key, value) -> System.out.println("THE KEY IS AND THE VLAUE IS " + key + " " + value));
			return rideRequestStream;

		};
	}

	@Bean
	public Function<KStream<String, AvroDriver>, KStream<String, AvroDriver>>  process_drivers() {
		return (avroDriverKStream) -> {
			avroDriverKStream.foreach((key, value) -> System.out.println("THE KEY IS AND THE VLAUE IS " + key + " " + value));
			return avroDriverKStream;

		};
	}



}
