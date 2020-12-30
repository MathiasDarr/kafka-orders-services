//package org.mddarr.ordersservice;
//
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
//import org.junit.Assert;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
////import org.mddarr.ordersservice.models.Order;
//
//
//public class OrderRepositoryTest extends AbstractIntegrationTest{
//
//    private static final Logger log = LoggerFactory.getLogger(OrderRepositoryTest.class);
//
//    @Autowired
//    AmazonDynamoDB dynamoDB;
//
////    @Autowired
////    OrderRepository orderRepository;
////
//    @BeforeEach
//    public void setup() throws IOException {
////        orderRepository.findAll().forEach(order -> orderRepository.delete(order.getOrderID(), order.getOrderCreationDate()));
////        DynamoUtils.loadOrdersData(dynamoDB);
//    }
//
//    @Test
//    public void contextLoads() {
//
//    }
////
////    @Test
////    public void testGetOrderByID(){
////        Optional<OrderRepository.OrderEntity> optional_order = orderRepository.get("20171129-29970",  20150135);
////        Assert.assertTrue(optional_order.isPresent());
////        OrderRepository.OrderEntity order = optional_order.get();
////        Assert.assertEquals(order.getOrderID(), "20171129-29970");
////        Assert.assertEquals(order.getOrderCreationDate(), 20150135);
////        Assert.assertEquals(order.getCustomerID(), "alice@gmail.com");
////    }
////
////    @Test
////    void testDeleteOrderByID(){
////        orderRepository.delete("20171129-29970",  20150135);
////        Optional<OrderRepository.OrderEntity> optional_order = orderRepository.get("20171129-29970",  20150135);
////        Assert.assertTrue(optional_order.isEmpty());
////    }
////
////    @Test
////    void testPostOrder(){
////        List<OrderRepository.OrderEntity> orders = orderRepository.findAll();
////        int number_of_orders_before_put = orders.size();
////        String customerID = "charles2@gmail.com";
////        List<String> brands = Arrays.asList("Osprey", "Osprey");
////        List<String> products = Arrays.asList("product1", "product2");
////        List<Long> quantities = Arrays.asList(1L,2L);
////        List<Double> prices = Arrays.asList(100.1, 205.2);
////        Order order = orderRepository.put(new OrderRequest(customerID, brands, products, quantities, prices));
////        orders = orderRepository.findAll();
////        int number_of_orders_after_put = orders.size();
////        Assert.assertEquals(number_of_orders_after_put, number_of_orders_before_put +1);
////    }
////
////    @Test
////    void testFetchCustomersOrders(){
////        List<OrderRepository.OrderEntity> aliceOrders = orderRepository.fetchCustomerOrders("alice@gmail.com");
////        List<OrderRepository.OrderEntity> jerryOrders = orderRepository.fetchCustomerOrders("dakobedbard@gmail.com");
////
////        for(OrderRepository.OrderEntity order: aliceOrders){
////            Assert.assertEquals(order.getCustomerID(), "alice@gmail.com");
////        }
////        for(OrderRepository.OrderEntity order: jerryOrders){
////            Assert.assertEquals(order.getCustomerID(), "dakobedbard@gmail.com");
////        }
////    }
//
//
//
////    @Test
////    void testFetchCustomerOrders(){
////        List<OrderRepository.OrderEntity> orders1 = orderRepository.fetchCustomerOrders("jerry@gmail.com", "20150115");
////        List<OrderRepository.OrderEntity> orders2 = orderRepository.fetchCustomerOrders("jerry@gmail.com", "20150125");
////        List<OrderRepository.OrderEntity> orders3 = orderRepository.fetchCustomerOrders("jerry@gmail.com", "20150130");
////
////        Assert.assertEquals(orders1.size(),2);
////        for(OrderRepository.OrderEntity order: orders1){
////            Assert.assertTrue(order.getOrderCreationDate() > 20150115 );
////        }
////        Assert.assertEquals(orders2.size(),1);
////        for(OrderRepository.OrderEntity order: orders2){
////            Assert.assertTrue(order.getOrderCreationDate() > 20150125 );
////        }
////        Assert.assertEquals(orders3.size(), 0);
////    }
////
//
//
////    @Test
////    void testFetchCustomerOrders(){
////        List<OrderRepository.OrderEntity> orders1 = orderRepository.fetchCustomerOrders("jerry@gmail.com", "20150115");
////        List<OrderRepository.OrderEntity> orders2 = orderRepository.fetchCustomerOrders("jerry@gmail.com", "20150125");
////        List<OrderRepository.OrderEntity> orders3 = orderRepository.fetchCustomerOrders("jerry@gmail.com", "20150130");
////
////        Assert.assertEquals(orders1.size(),2);
////        for(OrderRepository.OrderEntity order: orders1){
////            Assert.assertTrue(order.getOrderCreationDate() > 20150115 );
////        }
////        Assert.assertEquals(orders2.size(),1);
////        for(OrderRepository.OrderEntity order: orders2){
////            Assert.assertTrue(order.getOrderCreationDate() > 20150125 );
////        }
////        Assert.assertEquals(orders3.size(), 0);
////    }
//}
