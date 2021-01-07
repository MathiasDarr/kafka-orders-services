package org.mddarr.materializedview.config;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ViewConfiguration {


//    @Bean
//    StreamsBuilderFactoryBean getBeanFactory(){
//        Map<String, Object> config = new HashMap<>();
//        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "default");
//        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
//
//        KafkaStreamsConfiguration kafkaStreamsConfiguration = new KafkaStreamsConfiguration(config);
//
////        StreamsBuilderFactoryBean streamsBuilderFactoryBean = new StreamsBuilderFactoryBean(kafkaStreamsConfiguration);
////        streamsBuilderFactoryBean.
//        return streamsBuilderFactoryBean;
//    }


//    @Bean
//    StreamsBuilderFactoryBean getBeanFactory(){
//
////        final StreamsBuilder builder = new StreamsBuilder();
////        KafkaStreams streams = new KafkaStreams(builder, config);
////
////        ReadOnlyKeyValueStore<String, Long> keyValueStore = streams.store("CountsKeyValueStore", QueryableStoreTypes.keyValueStore());
//
//
////        config.putAll(Map.of(StreamsConfig.APPLICATION_ID_CONFIG, "test-app-2", StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092"));
////
////        Map<String, String> = Map.
////        KafkaStreamsConfiguration kafkaStreamsConfiguration = new KafkaStreamsConfiguration();
//
//

////        return streamsBuilderFactoryBean;
//    }
}
