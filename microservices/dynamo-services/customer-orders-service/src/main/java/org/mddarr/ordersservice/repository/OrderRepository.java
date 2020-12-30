package org.mddarr.ordersservice.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mddarr.ordersservice.models.Order;
import org.mddarr.ordersservice.models.OrderRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {
    private final static String TABLE = "Orders";
    private static final Logger log = LoggerFactory.getLogger(OrderRepository.class);

    private final AmazonDynamoDB amazonDynamoDB;
    private final DynamoDBMapper mapper;

    public OrderRepository(AmazonDynamoDB db){
        this.amazonDynamoDB = db;
        this.mapper = new DynamoDBMapper(amazonDynamoDB);
    }

    public List<OrderEntity> findAll() {
        DynamoDBMapperConfig mapperConfig = new DynamoDBMapperConfig.Builder()
                .withTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement(TABLE)).build();
        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB, mapperConfig);
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        List <OrderEntity> orders = mapper.scan(OrderEntity.class, scanExpression);
        return orders;
    }
//
//    public List<OrderEntity> fetchCustomerOrders(String customerID, String orderDate){
//
//    }



//    public List<OrderEntity> fetchCustomerOrders(String customerID, String orderDate){
//        Map<String, AttributeValue> vals = new HashMap<>();
//        vals.put(":cid", new AttributeValue().withS(customerID));
//        vals.put(":orderDate", new AttributeValue().withN(orderDate));
//
//        DynamoDBQueryExpression<OrderEntity> queryExp = new DynamoDBQueryExpression<OrderEntity>()
//                .withKeyConditionExpression("customerID = :cid and orderCreationDate > :orderDate")
//                .withIndexName("customerOrderIndex")
//                .withConsistentRead(false)
//                .withExpressionAttributeValues(vals);
//        List<OrderEntity> orders =  mapper.query(OrderEntity.class, queryExp);
//        return orders;
//    }

    public List<OrderEntity> fetchCustomerOrders(String customerID){
        Map<String, AttributeValue> vals = new HashMap<>();
        vals.put(":cid", new AttributeValue().withS(customerID));

        DynamoDBQueryExpression<OrderEntity> queryExp = new DynamoDBQueryExpression<OrderEntity>()
                .withKeyConditionExpression("customerID = :cid")
                .withConsistentRead(false)
                .withExpressionAttributeValues(vals);
        List<OrderEntity> orders =  mapper.query(OrderEntity.class, queryExp);
        return orders;
    }

    String customerID;
    List<String> productBrands;
    List<String> productsNames;
    List<Long> quantities;
    List<Double> prices;

    public Order put(OrderRequest order) {
        String orderID = UUID.randomUUID().toString();
        long creationDate = System.currentTimeMillis();
        mapper.save(new OrderEntity(orderID, creationDate, order.getCustomerID(), order.getVendors(), order.getProducts(),order.getQuantities(), "PENDING"));
        return new Order(orderID, creationDate, order.getCustomerID(), order.getVendors(), order.getProducts(),order.getQuantities(), "PENDING");
    }

    public Optional<OrderEntity> get(String orderID, long creationDate) {
        OrderEntity entity = mapper.load(OrderEntity.class, orderID, creationDate);
        return Optional.ofNullable(entity);
    }

    public void delete(String orderID, long orderCreationDate) {
        mapper.delete(new OrderEntity(orderID, orderCreationDate));
    }

    @DynamoDBTable(tableName=TABLE)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderEntity {

        @DynamoDBHashKey(attributeName="orderID")
        private String orderID;
        @DynamoDBRangeKey(attributeName = "orderCreationDate")
        private long orderCreationDate;
        @DynamoDBIndexHashKey(globalSecondaryIndexName = "customerOrderIndex", attributeName = "customerID")
        private String customerID;
        @DynamoDBAttribute(attributeName="vendors")
        private List<String> vendors;
        @DynamoDBAttribute(attributeName="products")
        private List<String> products;
        @DynamoDBAttribute(attributeName="quantities")
        private List<Long> quantities;

        @DynamoDBAttribute(attributeName="order_status")
        private String order_status;

        public OrderEntity(String orderID, long creationDate){
            this.orderID = orderID;
            this.orderCreationDate = creationDate;
        }
    }
}
