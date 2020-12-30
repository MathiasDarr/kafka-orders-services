package org.mddarr.inventoryservice.repository;


import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mddarr.inventoryservice.dto.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Repository
public class ProductRepository {

    private static final String TABLE = "Products";
    private static final Logger log = LoggerFactory.getLogger(ProductRepository.class);

    private final AmazonDynamoDB amazonDynamoDB;
    private final DynamoDBMapper mapper;
    private final DynamoDB dynamoDB;


    public ProductRepository(AmazonDynamoDB db){
        this.amazonDynamoDB = db;
        this.mapper = new DynamoDBMapper(amazonDynamoDB);
        this.dynamoDB = new DynamoDB(this.amazonDynamoDB);

    }

    public Optional<ProductEntity> get(String brand, String productName){
        ProductEntity entity = mapper.load(ProductEntity.class, brand, productName);
        return Optional.ofNullable(entity);
    }


    public List<ProductEntity> fetchAllProducts(){
        DynamoDBMapperConfig mapperConfig = new DynamoDBMapperConfig.Builder()
                .withTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement(TABLE)).build();
        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB, mapperConfig);
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        List <ProductEntity> products = mapper.scan(ProductEntity.class, scanExpression);
        return products;
    }

    public List<ProductEntity> fetchAllProductsByBrand(String brand){
        Map<String, AttributeValue> vals = new HashMap<>();
        vals.put(":brand", new AttributeValue().withS(brand));

        DynamoDBQueryExpression<ProductEntity> queryExp = new DynamoDBQueryExpression<ProductEntity>()
                .withKeyConditionExpression("vendor = :brand")
                .withConsistentRead(false)
                .withExpressionAttributeValues(vals);
        List<ProductEntity> products =  mapper.query(ProductEntity.class, queryExp);
        return products;
    }

    public List<ProductEntity> fetchAllProductsByCategory(String category){
        Map<String, AttributeValue> vals = new HashMap<>();
        vals.put(":category", new AttributeValue().withS(category));
        DynamoDBQueryExpression<ProductEntity> queryExp = new DynamoDBQueryExpression<ProductEntity>()
                .withKeyConditionExpression("category = :category")
                .withIndexName("categoryGSI")
                .withConsistentRead(false)
                .withExpressionAttributeValues(vals);
        List<ProductEntity> products =  mapper.query(ProductEntity.class, queryExp);
        return products;
    }

    public List<Category> fetchAllCategories(){
        ScanRequest scanRequest = new ScanRequest()
                .withTableName("Categories");

        List<Category> categories = new ArrayList<>();
        ScanResult result = amazonDynamoDB.scan(scanRequest);
        for (Map<String, AttributeValue> item : result.getItems()) {
            AttributeValue category = item.get("category");
            AttributeValue subcategory = item.get("subcategory");
            AttributeValue gender = item.get("gender");
            categories.add(new Category(category.toString(), subcategory.toString(), gender.toString()));
        }
        return categories;
    }


    @DynamoDBTable(tableName=TABLE)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductEntity {

        @DynamoDBHashKey(attributeName="vendor")
        private String vendor;
        @DynamoDBRangeKey(attributeName = "productName")
        private String productName;

//        @DynamoDBIndexHashKey(globalSecondaryIndexName = "productBrandIndex", attributeName = "productBrand")
//        private String productBrand;

        @DynamoDBAttribute(attributeName="price")
        private Double price;
//
//        @DynamoDBAttribute(attributeName="colors")
//        private List<String> colors;

        @DynamoDBAttribute(attributeName="category")
        private String category;


//        @DynamoDBAttribute(attributeName="imageURL")
//        private String imageURL;

    }
}
