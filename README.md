# ECommerce Kafka MicroServices #

This repository contains a Spring Boot microservices project. 

### The microservices are implemented using the following technologies ###
* Spring Boot
* Kafka, spring cloud Kafka binder, AVRO serialization
* Implementations using Cassandra or alternatively using DynamoDB & Java AWS SDK
* Integration tests using EmbeddedKafka
* Ehcache
* Jmeter for performance testing 

### Spring Microservices ###

* event-producer 
   - This module contains a main class which populates the inventory & orders topic
        - datastax cassandra client for reading from database 
        
* orders-service
    - expose endpoints for placing orders
    - writes to 'orders' topic  
* inventory-service
    - exposes endpoints for posting new items & updating stock 
    - products are written to 'inventory' topic
* orders-query-service
    - expose endpoints for querying customer orders
* product-query-service
    - expose endpoints for querying the product catalog

* DynamoDB backend


### Project Dependencies ###
* Java 11 & maven (if compiling & running natively instead of through Docker)
* docker-compose

### Running the microservices ###
* Ensure that the DynamoDB products table has been populated.  This can be done following the instructions in the data_model/products directory. 
* Launch zookeeper, kafka broker & schema registry in docker
    * docker-compose -f kafka-compose.yaml up 
* Compile 
    * mvn clean package

* Integration tests
    - test suite utilizes the python requests module to invoke the Lambda function via the API Gateway resouce & method
    - use requests to upload file file using the presigned post url returned by the lambda function 



### Run the integration tests ###


