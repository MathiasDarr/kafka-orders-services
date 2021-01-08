package org.mddarr.ordersviews.views;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.mddarr.ordersviews.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class Views {

    @Component
    public static class InventoryView {

        @Autowired
        public void buildInventoryView(StreamsBuilder builder) {
            builder.table(Constants.PRODUCT_INVENTORY_TOPIC,
                    Consumed.with(Serdes.Integer(), Serdes.String()),
                    Materialized.as(Constants.PRODUCT_INVENTORY_STORE));
        }
    }


    @Component
    public static class PurchaseCountView {

        @Autowired
        public void buildPurchaseCountView(StreamsBuilder builder) {
            builder.table(Constants.PURCHASE_COUNT_TOPIC,
                    Consumed.with(Serdes.Integer(), Serdes.String()),
                    Materialized.as(Constants.PURCHASE_COUNT_STORE));
        }
    }



}
