package org.mddarr.ordersviews;

import org.apache.kafka.streams.kstream.ValueTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.mddarr.orders.event.dto.AvroOrder;
import org.mddarr.orders.event.dto.AvroOrderResult;
import org.mddarr.products.AvroInventory;
import org.mddarr.transactions.AvroTransaction;
import org.mddarr.transactions.AvroTransactionResult;

@Slf4j
public class OrderValidationTransformer implements ValueTransformer<AvroOrder, AvroOrderResult>{

    private final String stateStoreName;

    private KeyValueStore<String, AvroInventory> store;


    public OrderValidationTransformer(String storeName ){
        stateStoreName = storeName;
    }

    @Override
    public void init(ProcessorContext context) {
        AvroTransaction avroTransaction = AvroTransaction.newBuilder()
                .setReceiver("Charles")
                .setSender("Erik")
                .setTransactionid("adf")
                .build();
        AvroTransactionResult avroTransactionResult = AvroTransactionResult.newBuilder()
                .setTransaction(avroTransaction)
                .setResult(false)
                .build();
        store = (KeyValueStore<String, AvroInventory>) context.getStateStore(stateStoreName);

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
