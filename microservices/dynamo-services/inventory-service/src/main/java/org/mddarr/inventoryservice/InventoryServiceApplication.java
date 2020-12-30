package org.mddarr.inventoryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SpringBootApplication
@EnableCaching
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/categories").allowedOrigins("http://localhost:8080");
			}
		};
	}

//	@Bean
//	public Consumer<String> sink1() {
//		return message -> {
//			System.out.println("******************");
//			System.out.println("At Sink1");
//			System.out.println("******************");
//			System.out.println("Received message " + message);
//		};
//	}
//
//	@Bean
//	public Consumer<String> sink2() {
//		return message -> {
//			System.out.println("******************");
//			System.out.println("At Sink2");
//			System.out.println("******************");
//			System.out.println("Received message " + message);
//		};
//	}
//
//	@Bean
//	public Supplier<String> source1() {
//		return () -> {
//			String message = "FromSource1";
//			System.out.println("******************");
//			System.out.println("From Source1");
//			System.out.println("******************");
//			System.out.println("Sending value: " + message);
//			return message;
//
//		};
//	}
//
//	@Bean
//	public Supplier<String> source2() {
//		return () -> {
//			String message = "FromSource2";
//			System.out.println("******************");
//			System.out.println("From Source2");
//			System.out.println("******************");
//			System.out.println("Sending value: " + message);
//			return message;
//
//		};
//	}

}
