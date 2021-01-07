package org.mddarr.reactive.views.views;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
class ViewConfiguration {

    @Autowired
    public void buildOrdersView(StreamsBuilder builder) {
        builder.table("orders",
                Consumed.with(Serdes.Integer(), Serdes.String()),
                Materialized.as("orders-store"));
    }
}