package org.mddarr.ordersviews.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.mddarr.ordersviews.Constants;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static org.apache.kafka.streams.StoreQueryParameters.fromNameAndType;
import static org.apache.kafka.streams.state.QueryableStoreTypes.keyValueStore;

@RestController
@RequiredArgsConstructor

public class OrdersController {

    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    @GetMapping("/iq/{id}")
    public String getOrder(@PathVariable final Integer id) {
        final KafkaStreams kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
        final ReadOnlyKeyValueStore<Integer, String> store = kafkaStreams.store(fromNameAndType(Constants.ORDERS_STORE , keyValueStore()));
        return store.get(id);
    }
}

