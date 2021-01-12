package org.mddarr.ordersviews;

import org.apache.kafka.streams.kstream.ValueTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.mddarr.orders.event.dto.AvroOrder;
import org.mddarr.orders.event.dto.AvroOrderResult;
import org.mddarr.products.AvroInventory;

@Slf4j
public class OrderTransformer implements ValueTransformer<AvroOrder, AvroOrderResult>{

    public OrderTransformer(){
    }

    @Override
    public void init(ProcessorContext processorContext) {

    }

    @Override
    public AvroOrderResult transform(AvroOrder avroOrder) {

        AvroOrderResult avroOrderResult = AvroOrderResult.newBuilder()
                .setId(avroOrder.getId())
                .setResult(true)
                .build();
        return avroOrderResult;
    }

    @Override
    public void close() {

    }
}
