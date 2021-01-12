package org.mddarr.ordersviews.topic;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:topics-defaults.yaml")
@Data
public abstract class TopicConfig {

  private String name;
  private boolean compacted;
  private int partitions;
  private short replicationFactor;

}
