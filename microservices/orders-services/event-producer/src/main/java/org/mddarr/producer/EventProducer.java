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
import org.mddarr.producer.models.Store;
import org.mddarr.producer.repository.*;


import org.mddarr.producer.runnable.PurchaseEventProducerThread;
import org.mddarr.products.AvroProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EventProducer {

    private static final Logger log = LoggerFactory.getLogger(EventProducer.class);

    private ExecutorService executor;
    private CountDownLatch latch;
//    private PurchaseEventProducerThread purchaseEventProducerThread;
    private List<PurchaseEventProducerThread>purchaseEventProducerThreads;
    public static void main(String[] args) throws Exception {
//        EventProducer app = new EventProducer(args);
//        app.start();
        populateSingleOrder();
    }

    public static void populateSingleOrder() throws Exception {

        KafkaGenericTemplate<AvroOrder> kafkaGenericTemplate = new KafkaGenericTemplate<>();
        KafkaTemplate<String, AvroOrder> ordersKafkaTemplate = kafkaGenericTemplate.getKafkaTemplate();
        ordersKafkaTemplate.setDefaultTopic(Constants.ORDERS_TOPIC);
        List<String> vendors = new ArrayList(Arrays.asList("vendor1"));
        List<String> products = new ArrayList(Arrays.asList("product1"));
        List<Long> quantities = new ArrayList(Arrays.asList(1L));


        AvroOrder avroOrder = AvroOrder.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setState(OrderState.PENDING)
                .setVendors(vendors)
                .setQuantites(quantities)
                .setProducts(products)
                .setPrice(120)
                .setCustomerId("Charles Goodwin")
                .build();
        System.out.println("i sent an order");
        ordersKafkaTemplate.sendDefault(avroOrder);

    }



    private EventProducer(String[] arguments){
        latch = new CountDownLatch(2);
        executor = Executors.newFixedThreadPool(2);
//        purchaseEventProducerThread = new PurchaseEventProducerThread(latch);
    }

    public void start(){
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!executor.isShutdown()) {
                log.info("Shutdown requested");
                shutdown();
            }
        }));


        CassandraConnector connector = new CassandraConnector();
        connector.connect("127.0.0.1", null);
        Session session = connector.getSession();
        KeyspaceRepository sr = new KeyspaceRepository(session);
        sr.useKeyspace("ks1");




        log.info("Application started!");
        StoreRepository storeRepository = new StoreRepository(session);

        List<Store> stores = storeRepository.selectAll();
        stores.forEach(store->{
            System.out.println("The store is " + store);
        });

        purchaseEventProducerThreads = new ArrayList<>();
        stores.forEach(store->{
            PurchaseEventProducerThread producerThread = new PurchaseEventProducerThread(latch, store);
            executor.submit(producerThread);
            purchaseEventProducerThreads.add(producerThread);
        });

        log.info("Stuff submit");
        try {
            log.info("Latch await");
            latch.await();
            log.info("Threads completed");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            shutdown();
            log.info("Application closed succesfully");
        }

    }


    private void shutdown() {
        if (!executor.isShutdown()) {
            log.info("Shutting down");
            executor.shutdownNow();
            try {
                if (!executor.awaitTermination(2000, TimeUnit.MILLISECONDS)) { //optional *
                    log.warn("Executor did not terminate in the specified time."); //optional *
                    List<Runnable> droppedTasks = executor.shutdownNow(); //optional **
                    log.warn("Executor was abruptly shut down. " + droppedTasks.size() + " tasks will not be executed."); //optional **
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
