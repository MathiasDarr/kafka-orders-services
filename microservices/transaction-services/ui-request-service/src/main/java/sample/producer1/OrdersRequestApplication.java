package sample.producer1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;

import org.mddarr.orders.event.dto.AvroOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.schema.registry.client.ConfluentSchemaRegistryClient;
import org.springframework.cloud.schema.registry.client.EnableSchemaRegistryClient;
import org.springframework.cloud.schema.registry.client.SchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableSchemaRegistryClient
public class OrdersRequestApplication {

	@Autowired
	BlockingQueue<AvroOrder> unbounded = new LinkedBlockingQueue<>();

	public static void main(String[] args) {
		SpringApplication.run(OrdersRequestApplication.class, args);
	}


	@Bean
	public Supplier<AvroOrder> orders_supplier() {
		return () -> unbounded.poll();
	}

	@Configuration
	static class ConfluentSchemaRegistryConfiguration {
		@Bean
		public SchemaRegistryClient schemaRegistryClient(@Value("${spring.cloud.stream.schemaRegistryClient.endpoint}") String endpoint){
			ConfluentSchemaRegistryClient client = new ConfluentSchemaRegistryClient();
			client.setEndpoint(endpoint);
			return client;
		}
	}
}



