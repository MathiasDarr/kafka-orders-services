# ECommerce Kafka MicroServices #

This repository contains a Spring Boot microservices project. 

### The microservices are implemented using the following technologies ###
* Spring Boot
* Kafka, spring cloud Kafka binder, AVRO serialization
* Implementations using Cassandra or alternatively using DynamoDB & Java AWS SDK
* Integration tests using EmbeddedKafka
* Ehcache
* Jmeter for performance testing 

### Microservices Microservices Architecture ###

<img src ="https://dakobed-images.s3-us-west-2.amazonaws.com/Orders+Microsrevices+Architecture.png" width ="1200" height="800">






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





### Run the integration tests ###


