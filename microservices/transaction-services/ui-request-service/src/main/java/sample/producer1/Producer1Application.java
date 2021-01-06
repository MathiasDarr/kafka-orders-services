package sample.producer1;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;

import com.example.Sensor;

import org.mddarr.orders.event.dto.FirstOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.schema.registry.client.ConfluentSchemaRegistryClient;
import org.springframework.cloud.schema.registry.client.EnableSchemaRegistryClient;
import org.springframework.cloud.schema.registry.client.SchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableSchemaRegistryClient
public class Producer1Application {

	@Autowired
	BlockingQueue<FirstOrder> unbounded = new LinkedBlockingQueue<>();

	public static void main(String[] args) {
		SpringApplication.run(Producer1Application.class, args);
	}


	@Bean
	public Supplier<FirstOrder> supplier() {
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



