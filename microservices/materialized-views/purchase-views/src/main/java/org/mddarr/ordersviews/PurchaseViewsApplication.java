package org.mddarr.ordersviews;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableKafka
@EnableKafkaStreams
public class PurchaseViewsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PurchaseViewsApplication.class, args);
	}

}

@Component
class InventoryView {

	@Autowired
	public void buildInventoryView(StreamsBuilder builder) {
		builder.table(Constants.PRODUCT_INVENTORY_TOPIC,
				Consumed.with(Serdes.Integer(), Serdes.String()),
				Materialized.as(Constants.PRODUCT_INVENTORY_STORE));
	}
}


@Component
class PurchaseCountView {

	@Autowired
	public void buildPurchaseCountView(StreamsBuilder builder) {
		builder.table(Constants.PURCHASE_COUNT_TOPIC,
				Consumed.with(Serdes.Integer(), Serdes.String()),
				Materialized.as(Constants.PURCHASE_COUNT_STORE));
	}
}



//@Component
//class InventoryView {
//
//	@Autowired
//	public void buildInventoryView(StreamsBuilder builder) {
//		builder.table("orders-second",
//				Consumed.with(Serdes.Integer(), Serdes.String()),
//				Materialized.as("inventory-store"));
//	}
//}


//
//@Component
//class ProductInventoryView {
//
//	@Autowired
//	public void buildProdutInventoryView(StreamsBuilder builder) {
//
//
//		builder.table( Constants.PRODUCT_INVENTORY_TOPIC,
//				Consumed.with(Serdes.Integer(), Serdes.String()),
//				Materialized.as(Constants.PRODUCT_INVENTORY_TOPIC));
//	}
//}
//
//@Component
//class PurchaseCountView {
//
//	@Autowired
//	public void buildOrdersView(StreamsBuilder builder) {
//		builder.table("orders",
//				Consumed.with(Serdes.Integer(), Serdes.String()),
//				Materialized.as("orders-store"));
//	}
//}






@Component
@RequiredArgsConstructor
class Producer {

	private final KafkaTemplate<Integer, String> kafkaTemplate;

	@EventListener(ApplicationStartedEvent.class)
	public void produce() {
		kafkaTemplate.send(Constants.PRODUCT_INVENTORY_TOPIC, 1, "iPad");
		kafkaTemplate.send(Constants.PRODUCT_INVENTORY_TOPIC, 2, "iPhone");
		kafkaTemplate.send(Constants.PRODUCT_INVENTORY_TOPIC, 1, "iPad, Airpods");
		kafkaTemplate.send(Constants.PRODUCT_INVENTORY_TOPIC, 2, "HomePod");
	}
}


@Component
@RequiredArgsConstructor
class PurchaseEventProducer {

	private final KafkaTemplate<Integer, String> kafkaTemplate;

	@EventListener(ApplicationStartedEvent.class)
	public void produce() {
		kafkaTemplate.send(Constants.PURCHASE_COUNT_TOPIC, 1, "iPad");
		kafkaTemplate.send(Constants.PURCHASE_COUNT_TOPIC, 2, "iPhone");
		kafkaTemplate.send(Constants.PURCHASE_COUNT_TOPIC, 1, "iPad, Airpods");
		kafkaTemplate.send(Constants.PURCHASE_COUNT_TOPIC, 2, "HomePod");
	}
}



