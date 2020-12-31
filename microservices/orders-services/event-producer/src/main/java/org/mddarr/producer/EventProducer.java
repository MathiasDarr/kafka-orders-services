package org.mddarr.producer;

import com.datastax.driver.core.Session;
import org.mddarr.producer.domain.Product;
import org.mddarr.producer.repository.ProductCassandraRepository;
import org.mddarr.producer.repository.KeyspaceRepository;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class EventProducer {

    private static final Logger LOG = LoggerFactory.getLogger(EventProducer.class);

    public static void main(String[] args) throws Exception {
        populateBooks();
    }
    public static void populateBooks(){
        CassandraConnector connector = new CassandraConnector();
        connector.connect("127.0.0.1", null);
        Session session = connector.getSession();

        KeyspaceRepository sr = new KeyspaceRepository(session);
//      sr.createKeyspace("library", "SimpleStrategy", 1);
        sr.useKeyspace("ks1");

        ProductCassandraRepository br = new ProductCassandraRepository(session);

        List<Product>  products = br.selectAll(); //.forEach(o -> LOG.info("Title in books: " + o.getTitle()));
        System.out.println("PRODUCTS " + products.size());
        connector.close();
    }
    public static void populateInventory(){

    }



//    public static void populateRideRequests() throws Exception{
//        final Map<String, String> serdeConfig = Collections.singletonMap(
//                AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
//        // Set serializers and
//        final SpecificAvroSerializer<AvroRideRequest> purchaseEventSerializer = new SpecificAvroSerializer<>();
//        purchaseEventSerializer.configure(serdeConfig, false);
//
//
//        Map<String, Object> props = new HashMap<>();
//        props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        props.put(ProducerConfig.RETRIES_CONFIG, 0);
//        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
//        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
//        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, purchaseEventSerializer.getClass());
//
//
//        DefaultKafkaProducerFactory<String, AvroRideRequest> pf1 = new DefaultKafkaProducerFactory<>(props);
//        KafkaTemplate<String, AvroRideRequest> rideRequestKafkaTemplate = new KafkaTemplate<>(pf1, true);
//        rideRequestKafkaTemplate.setDefaultTopic(Constants.RIDE_REQUEST_TOPIC);
//
//        List<AvroRideRequest> rideRequests = DataService.getRideRequestsFromDB();
//
//        rideRequests.forEach(rideRequest -> {
//            System.out.println("Writing ride request for '" + rideRequest.getRequestId() + "' to input topic " +
//                    Constants.RIDE_REQUEST_TOPIC);
//            rideRequestKafkaTemplate.sendDefault(rideRequest);
//        });
//    }
//
//
//
//    public static void populateDrivers() throws Exception{
//        final Map<String, String> serdeConfig = Collections.singletonMap(
//                AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
//        // Set serializers and
//        final SpecificAvroSerializer<AvroDriver> purchaseEventSerializer = new SpecificAvroSerializer<>();
//        purchaseEventSerializer.configure(serdeConfig, false);
//
//
//        Map<String, Object> props = new HashMap<>();
//        props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        props.put(ProducerConfig.RETRIES_CONFIG, 0);
//        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
//        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
//        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, purchaseEventSerializer.getClass());
//
//
//        DefaultKafkaProducerFactory<String, AvroDriver> pf1 = new DefaultKafkaProducerFactory<>(props);
//        KafkaTemplate<String, AvroDriver> driverKafkaTemplate = new KafkaTemplate<>(pf1, true);
//        driverKafkaTemplate.setDefaultTopic(Constants.DRIVERS_TOPIC);
//
//        List<AvroDriver> drivers = DataService.getProductsFromDB();
//
//        drivers.forEach(driver -> {
//            System.out.println("Writing driver for '" + driver.getFirstname() + "' to input topic " +
//                    Constants.DRIVERS_TOPIC);
//            driverKafkaTemplate.sendDefault(driver);
//        });
//    }

}
