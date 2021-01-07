package org.mddarr.ridereceiver;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.mddarr.orders.event.dto.FirstOrder;
import org.mddarr.products.AvroProduct;
import org.mddarr.products.AvroPurchaseCount;
import org.mddarr.products.AvroPurchaseEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@SpringBootApplication
public class MaterializedViewApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaterializedViewApplication.class, args);
	}

	@Bean
	public BiConsumer<KTable<String, AvroProduct>, KStream<String, AvroPurchaseEvent>> process_products() {
		return((avroProductKTable, purchaseEventKStream) -> {

			// Configure the SpecificAvroSerdes
			final Map<String, String> serdeConfig = Collections.singletonMap(
					AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

			final SpecificAvroSerde<AvroPurchaseEvent> playEventSerde = new SpecificAvroSerde<>();
			playEventSerde.configure(serdeConfig, false);

			final SpecificAvroSerde<AvroProduct> keySongSerde = new SpecificAvroSerde<>();
			keySongSerde.configure(serdeConfig, true);

			final SpecificAvroSerde<AvroProduct> productAvroSerde = new SpecificAvroSerde<>();
			productAvroSerde.configure(serdeConfig, false);

			final SpecificAvroSerde<AvroPurchaseCount> songPlayCountSerde = new SpecificAvroSerde<>();
			songPlayCountSerde.configure(serdeConfig, false);

			final KStream<String, AvroPurchaseEvent> purchaseByItemID =
					purchaseEventKStream
							// repartition based on song id
							.map((key, value) -> KeyValue.pair(value.getVendor() + value.getProduct(), value));
			purchaseByItemID.foreach((k,v)->{
				System.out.println(v);
			});

			purchaseByItemID.leftJoin(avroProductKTable,(value,song)->song,Joined.with(Serdes.String(),playEventSerde, productAvroSerde));




			//			final KStream<String, AvroProduct> productPurchases = purchaseByItemID.leftJoin(avroProductKTable,
//					(value1, song) -> song,
//					Joined.with(Serdes.Long(), playEventSerde, valueSongSerde));




			System.out.println("HEAEEEEY AAA");


//			final KTable<Song, Long> songPlayCounts = songPlays.groupBy((songId, song) -> song,
//					Serialized.with(keySongSerde, productAvroSerde))
//					.count(Materialized.<Song, Long, KeyValueStore<Bytes, byte[]>>as(SONG_PLAY_COUNT_STORE)
//							.withKeySerde(productAvroSerde)
//							.withValueSerde(Serdes.Long()));
//
//
//



		});
	}


}
