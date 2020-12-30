package org.mddarr.inventoryservice;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mddarr.inventoryservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class InventoryServiceApplicationTests {

	@Autowired
	AmazonDynamoDB dynamoDB;

	@Autowired
	ProductRepository productRepository;


	@BeforeEach
	public void setup() throws IOException {

	}

	@Test
	void testQueryByProductBrand(){
		List<ProductRepository.ProductEntity> osprey_products = productRepository.fetchAllProductsByBrand("Osprey");
		List<ProductRepository.ProductEntity> northface_products = productRepository.fetchAllProductsByBrand("North Face");
		List<ProductRepository.ProductEntity> mountain_engineering_products = productRepository.fetchAllProductsByBrand("Mountain Equipment");

		for(ProductRepository.ProductEntity product: osprey_products){
			Assert.assertEquals(product.getVendor(), "Osprey" );
		}
		for(ProductRepository.ProductEntity product: northface_products){
			Assert.assertEquals(product.getVendor(), "North Face");
		}
		for(ProductRepository.ProductEntity product: mountain_engineering_products){
			Assert.assertEquals(product.getVendor(), "Mountain Equipment");
		}
	}

	@Test
	void contextLoads() {
	}


}
