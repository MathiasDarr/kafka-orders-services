package org.mddarr.ordersviews.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.mddarr.ordersviews.Constants;
import org.mddarr.ordersviews.models.InventoryDetail;
import org.mddarr.ordersviews.models.PurchaseCountDetail;
import org.mddarr.products.AvroInventory;
import org.mddarr.products.AvroPurchaseCount;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static org.apache.kafka.streams.StoreQueryParameters.fromNameAndType;
import static org.apache.kafka.streams.state.QueryableStoreTypes.keyValueStore;


@RestController
@RequiredArgsConstructor
public class PurchaseCountController {

    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    @GetMapping("/iq/{id}")
    public String getOrder(@PathVariable final Integer id) {
        final KafkaStreams kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
        final ReadOnlyKeyValueStore<Integer, String> store = kafkaStreams.store(fromNameAndType(Constants.ORDERS_STORE , keyValueStore()));
        return store.get(id);
    }

    @GetMapping("/products/inventory/{productid}")
    public InventoryDetail getProductInventoryDetail(@PathVariable final String productid) {
        final KafkaStreams kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
        final ReadOnlyKeyValueStore<String, AvroInventory> store =
                kafkaStreams.store(fromNameAndType(Constants.PRODUCT_INVENTORY_STORE , keyValueStore()));

        AvroInventory avroInventory = store.get(productid);
        InventoryDetail inventoryDetail = new InventoryDetail();
        inventoryDetail.setProductid(avroInventory.getProductid());
        inventoryDetail.setInventory(avroInventory.getInventory());
        return inventoryDetail;
    }


    @GetMapping("/purchases/count/{productid}")
    public PurchaseCountDetail getProductPurchaseCountDetail(@PathVariable final String productid) {
        final KafkaStreams kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
        final ReadOnlyKeyValueStore<String, AvroPurchaseCount> store =
                kafkaStreams.store(fromNameAndType(Constants.PURCHASE_COUNT_STORE , keyValueStore()));

        AvroPurchaseCount avroPurchaseCount = store.get(productid);
        PurchaseCountDetail purchaseCountDetail = new PurchaseCountDetail();
        purchaseCountDetail.setProductid(avroPurchaseCount.getProductId());
        purchaseCountDetail.setCount(avroPurchaseCount.getCount());
        return purchaseCountDetail;
    }

}
