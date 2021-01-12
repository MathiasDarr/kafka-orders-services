package org.mddarr.ordersviews.topic;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;



@Configuration
public class OrdersTopic extends TopicConfig {
    String name = "ORDERS-IN-TOPIC";

    @Override
    public String getName() {
        return name;
    }
}
