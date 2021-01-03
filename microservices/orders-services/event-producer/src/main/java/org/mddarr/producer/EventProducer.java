package org.mddarr.producer;

import com.datastax.driver.core.Session;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.mddarr.customers.AvroCustomer;
import org.mddarr.orders.event.dto.AvroOrder;
import org.mddarr.orders.event.dto.OrderState;
import org.mddarr.producer.config.CassandraConnector;
import org.mddarr.producer.kafka.templates.KafkaGenericTemplate;
import org.mddarr.producer.models.Customer;
import org.mddarr.producer.models.Order;
import org.mddarr.producer.models.Product;
import org.mddarr.producer.repository.CustomerRepository;
import org.mddarr.producer.repository.OrderRepository;
import org.mddarr.producer.repository.ProductRepository;
import org.mddarr.producer.repository.KeyspaceRepository;


import org.mddarr.products.AvroProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.*;

public class EventProducer {

    private static final Logger LOG = LoggerFactory.getLogger(EventProducer.class);

    public static void main(String[] args) throws Exception {
//        populateOrders();
        populateProducts();
    }
    public static void populateCustomers(){

        CassandraConnector connector = new CassandraConnector();
        connector.connect("127.0.0.1", null);

        Session session = connector.getSession();
        KeyspaceRepository sr = new KeyspaceRepository(session);
        sr.useKeyspace("ks1");

        KafkaGenericTemplate<AvroCustomer> kafkaGenericTemplate = new KafkaGenericTemplate<>();
        KafkaTemplate<String, AvroCustomer> customerKafkaTemplate = kafkaGenericTemplate.getKafkaTemplate();
        customerKafkaTemplate.setDefaultTopic(Constants.CUSTOMERS_TOPIC);

        CustomerRepository customerRepository = new CustomerRepository(session);
        List<Customer> customers = customerRepository.selectAll();

        customers.forEach(customer -> {
            System.out.println("Writing customeer for '" + customer.getCustomerid() + "' to input topic " +
                    Constants.CUSTOMERS_TOPIC);
            customerKafkaTemplate.sendDefault(new AvroCustomer(customer.getCustomerid(), customer.getCity()));
        });

        connector.close();
    }



    public static void populateProducts() throws Exception {
        CassandraConnector connector = new CassandraConnector();
        connector.connect("127.0.0.1", null);
        Session session = connector.getSession();

        KeyspaceRepository sr = new KeyspaceRepository(session);
        sr.useKeyspace("ks1");

        ProductRepository br = new ProductRepository(session);
        List<Product>  products = br.selectAll();
        List<AvroProduct> avroProducts = ProductRepository.mapAvroProducts(products);

        KafkaGenericTemplate<AvroProduct> kafkaGenericTemplate = new KafkaGenericTemplate<>();
        KafkaTemplate<String, AvroProduct> productKafkaTemplate = kafkaGenericTemplate.getKafkaTemplate();
        productKafkaTemplate.setDefaultTopic(Constants.PRODUCTS_TOPIC);

        avroProducts.forEach(product -> {
            System.out.println("Writing inventory for '" + product.getProduct() + "' to input topic " + Constants.PRODUCTS_TOPIC);
            productKafkaTemplate.sendDefault(product);
        });

        connector.close();
    }

    public static void populateOrders() throws Exception {
        CassandraConnector connector = new CassandraConnector();
        connector.connect("127.0.0.1", null);
        Session session = connector.getSession();

        KeyspaceRepository sr = new KeyspaceRepository(session);
        sr.useKeyspace("ks1");

        OrderRepository br = new OrderRepository(session);

        List<Order>  orders = br.selectAll();
        List<AvroOrder> avroOrders = OrderRepository.mapAvroOrders(orders);

        KafkaGenericTemplate<AvroOrder> kafkaGenericTemplate = new KafkaGenericTemplate<>();
        KafkaTemplate<String, AvroOrder> ordersKafkaTemplate = kafkaGenericTemplate.getKafkaTemplate();
        ordersKafkaTemplate.setDefaultTopic(Constants.ORDERS_TOPIC);

        avroOrders.forEach(order -> {
            System.out.println("Writing Order for customer '" + order.getCustomerId() + "' to input topic " + Constants.ORDERS_TOPIC);
            ordersKafkaTemplate.sendDefault(order);
        });

        connector.close();
    }

}
