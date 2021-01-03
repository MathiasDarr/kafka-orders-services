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
import org.mddarr.producer.repository.OrderCassandraRepository;
import org.mddarr.producer.repository.ProductCassandraRepository;
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
        populateCustomers();
        //        populateProducts();
    }
    public static void populateCustomers(){

        CassandraConnector connector = new CassandraConnector();
        connector.connect("127.0.0.1", null);
        Session session = connector.getSession();

        KeyspaceRepository sr = new KeyspaceRepository(session);
//      sr.createKeyspace("library", "SimpleStrategy", 1);
        sr.useKeyspace("ks1");

        KafkaGenericTemplate<AvroCustomer> kafkaGenericTemplate = new KafkaGenericTemplate<>();
        KafkaTemplate<String, AvroCustomer> driverKafkaTemplate = kafkaGenericTemplate.getKafkaTemplate();
        driverKafkaTemplate.setDefaultTopic(Constants.CUSTOMERS_TOPIC);


        CustomerRepository customerRepository = new CustomerRepository(session);
        List<Customer> customers = customerRepository.selectAll();

        customers.forEach(customer -> {
            System.out.println("Writing customeer for '" + customer.getCustomerid() + "' to input topic " +
                    Constants.CUSTOMERS_TOPIC);
            driverKafkaTemplate.sendDefault(new AvroCustomer(customer.getCustomerid(), customer.getCity()));
        });

        connector.close();
    }



    public static List<AvroProduct> mapAvroProducts(List<Product> products){
        List<AvroProduct> avroProducts = new ArrayList<>();
        for(Product product: products){
            AvroProduct avroProduct = AvroProduct.newBuilder()
                    .setProduct(product.getProduct())
                    .setInventory(product.getInventory())
                    .setPrice(product.getPrice())
                    .setVendor(product.getVendor())
                    .build();
            avroProducts.add(avroProduct);
        }
        return avroProducts;
    }

    public static List<AvroOrder> mapAvroOrders(List<Order> orders){
        List<AvroOrder> avroOrders = new ArrayList<>();
        for(Order order: orders){
            AvroOrder avroOrder = AvroOrder.newBuilder()
                    .setId(UUID.randomUUID().toString())
                    .setCustomerId(order.getCustomerID())
                    .setVendors(order.getVendors())
                    .setPrice(12.1)
                    .setState(OrderState.PENDING)
                    .setProducts(order.getProducts())
                    .setQuantites(order.getQuantities())
                    .build();
            avroOrders.add(avroOrder);
        }
        return avroOrders;
    }


    public static void populateProducts() throws Exception {
        CassandraConnector connector = new CassandraConnector();
        connector.connect("127.0.0.1", null);
        Session session = connector.getSession();

        KeyspaceRepository sr = new KeyspaceRepository(session);
//      sr.createKeyspace("library", "SimpleStrategy", 1);
        sr.useKeyspace("ks1");

        ProductCassandraRepository br = new ProductCassandraRepository(session);

        List<Product>  products = br.selectAll(); //.forEach(o -> LOG.info("Title in books: " + o.getTitle()));
        List<AvroProduct> avroProducts = mapAvroProducts(products);
        populateKafkaInventoryTopic(avroProducts);
        connector.close();
    }

    public static void populateOrders() throws Exception {
        CassandraConnector connector = new CassandraConnector();
        connector.connect("127.0.0.1", null);
        Session session = connector.getSession();

        KeyspaceRepository sr = new KeyspaceRepository(session);
//      sr.createKeyspace("library", "SimpleStrategy", 1);
        sr.useKeyspace("ks1");

        OrderCassandraRepository br = new OrderCassandraRepository(session);

        List<Order>  orders = br.selectAll(); //.forEach(o -> LOG.info("Title in books: " + o.getTitle()));
        List<AvroOrder> avroOrders = mapAvroOrders(orders);
        populateKafkaOrdersTopic(avroOrders);
        connector.close();
    }


    public static void populateKafkaInventoryTopic(List<AvroProduct> avroProducts) throws Exception{
        final Map<String, String> serdeConfig = Collections.singletonMap(
                AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        // Set serializers and
        final SpecificAvroSerializer<AvroProduct> productSerializer = new SpecificAvroSerializer<>();
        productSerializer.configure(serdeConfig, false);

        Map<String, Object> props = new HashMap<>();
        props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, productSerializer.getClass());

        DefaultKafkaProducerFactory<String, AvroProduct> pf1 = new DefaultKafkaProducerFactory<>(props);
        KafkaTemplate<String, AvroProduct> productKafkaTemplate = new KafkaTemplate<>(pf1, true);
        productKafkaTemplate.setDefaultTopic(Constants.PRODUCTS_TOPIC);

        avroProducts.forEach(product -> {
            System.out.println("Writing ride request for '" + product.getProduct() + "' to input topic " + Constants.PRODUCTS_TOPIC);
            productKafkaTemplate.sendDefault(product);
        });
    }


    public static void populateKafkaOrdersTopic(List<AvroOrder> avroOrders) throws Exception{
        final Map<String, String> serdeConfig = Collections.singletonMap(
                AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        // Set serializers and
        final SpecificAvroSerializer<AvroOrder> productSerializer = new SpecificAvroSerializer<>();
        productSerializer.configure(serdeConfig, false);

        Map<String, Object> props = new HashMap<>();
        props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, productSerializer.getClass());

        DefaultKafkaProducerFactory<String, AvroOrder> pf1 = new DefaultKafkaProducerFactory<>(props);
        KafkaTemplate<String, AvroOrder> productKafkaTemplate = new KafkaTemplate<>(pf1, true);
        productKafkaTemplate.setDefaultTopic(Constants.ORDERS_TOPIC);

        avroOrders.forEach(order -> {
            System.out.println("Writing ride request for '" + order.getCustomerId() + "' to input topic " + Constants.ORDERS_TOPIC);
            productKafkaTemplate.sendDefault(order);
        });
    }

}
