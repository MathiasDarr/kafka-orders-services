package org.mddarr.materializedview.controllers;


import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreType;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.mddarr.products.AvroPurchaseCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PurchaseEventController {
//

//    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;
//

    @Autowired
    StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    @GetMapping("/purchase/counts/{id}")
    public AvroPurchaseCount getPurchaseCounts(@PathVariable final String id){
        final KafkaStreams kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
        final ReadOnlyKeyValueStore<String, AvroPurchaseCount> store = kafkaStreams.store(StoreQueryParameters
                .fromNameAndType("purchase-counts", QueryableStoreTypes.keyValueStore()));

        return store.get(id);
    }
//

}
