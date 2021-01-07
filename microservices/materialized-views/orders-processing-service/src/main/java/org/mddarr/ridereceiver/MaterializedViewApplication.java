package org.mddarr.ridereceiver;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.mddarr.orders.event.dto.FirstOrder;
import org.mddarr.products.AvroProduct;
import org.mddarr.products.AvroPurchaseCount;
import org.mddarr.products.AvroPurchaseEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@SpringBootApplication
@EnableKafkaStreams
@EnableKafka
public class MaterializedViewApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaterializedViewApplication.class, args);
	}

	private static class PurchaseStreamsMaterializedViewApplication{


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
								// repartition based on product id
								.map((key, value) -> KeyValue.pair(value.getVendor() + value.getProduct(), value));
				purchaseByItemID.foreach((k,v)->{
					System.out.println(v);
				});

				final KStream<String, AvroProduct> pruchaseStream = purchaseByItemID
						.leftJoin(avroProductKTable,(value, product) -> product,
						Joined.with(Serdes.String(),playEventSerde, productAvroSerde));

				final KTable<AvroProduct, Long> productsPurchaseCounts = pruchaseStream.groupBy((productid, product) -> product
						,Grouped.with(keySongSerde, productAvroSerde))
						.count(Materialized.<AvroProduct, Long, KeyValueStore<Bytes, byte[]>>as(Constants.PURCHASE_EVENT_COUNT_STORE)
							.withKeySerde(productAvroSerde)
							.withValueSerde(Serdes.Long()));

				final TopFiveSerde topFiveSerde = new TopFiveSerde();

				// Compute the top five charts for each genre. The results of this computation will continuously update the state
				// store "top-five-songs-by-genre", and this state store can then be queried interactively via a REST API (cf.
				// MusicPlaysRestService) for the latest charts per genre.
				productsPurchaseCounts.groupBy((song, purchases) ->
								KeyValue.pair(Constants.TOP_FIVE_KEY,
										new AvroPurchaseCount(song.getVendor()+song.getProduct(), purchases)),
						Grouped.with(Serdes.String(), songPlayCountSerde))
						.aggregate(TopFiveProducts::new,
								(aggKey, value, aggregate) -> {
									aggregate.add(value);
									return aggregate;
								},
								(aggKey, value, aggregate) -> {
									aggregate.remove(value);
									return aggregate;
								},
								Materialized.<String, TopFiveProducts, KeyValueStore<Bytes, byte[]>>as(Constants.TOP_FIVE_PRODUCTS_STORE)
										.withKeySerde(Serdes.String())
										.withValueSerde(topFiveSerde)
						);
			});
		}
	}


	private static class TopFiveSerde implements Serde<TopFiveProducts> {

		@Override
		public Serializer<TopFiveProducts> serializer() {

			return new Serializer<TopFiveProducts>() {
				@Override
				public void configure(final Map<String, ?> map, final boolean b) {
				}

				@Override
				public byte[] serialize(final String s, final TopFiveProducts topFiveSongs) {

					final ByteArrayOutputStream out = new ByteArrayOutputStream();
					final DataOutputStream
							dataOutputStream =
							new DataOutputStream(out);
					try {
						for (AvroPurchaseCount purchaseCount : topFiveSongs) {
							dataOutputStream.writeUTF(purchaseCount.getProductId());
							dataOutputStream.writeLong(purchaseCount.getCount());
						}
						dataOutputStream.flush();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					return out.toByteArray();
				}
			};
		}

		@Override
		public Deserializer<TopFiveProducts> deserializer() {

			return (s, bytes) -> {
				if (bytes == null || bytes.length == 0) {
					return null;
				}
				final TopFiveProducts result = new TopFiveProducts();

				final DataInputStream
						dataInputStream =
						new DataInputStream(new ByteArrayInputStream(bytes));

				try {
					while(dataInputStream.available() > 0) {
						result.add(new AvroPurchaseCount(dataInputStream.readUTF(),
								dataInputStream.readLong()));
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				return result;
			};
		}
	}


}


@Component
class OrderView {
	public void buildOrdersView(StreamsBuilder builder){
		builder.table("topic", Consumed.with(Serdes.Integer(), Serdes.String()), Materialized.as("orders-store"));

	}
}

//@Component
//@RequiredArgsConstructor
//class Producer{
//	private final KafkaTemplate kafkaTemplate;
//	@EventListener(ApplicationStartedEvent.class)
//	public void produce(){
//
//	}
//}
//
//
