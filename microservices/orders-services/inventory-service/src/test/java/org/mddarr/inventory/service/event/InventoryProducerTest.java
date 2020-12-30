package org.mddarr.inventory.service.event;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.mddarr.inventory.service.Constants;
import org.mddarr.inventory.service.UatAbstractTest;
import org.mddarr.inventory.service.models.ProductMessage;
import org.mddarr.inventory.service.services.AvroOrderRequestProducer;
import org.mddarr.orders.event.dto.AvroOrder;

import org.mddarr.products.AvroProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class InventoryProducerTest extends UatAbstractTest {

    @Autowired
    private AvroOrderRequestProducer avroRideRequestProducer;

    @Test
    public void should_send_inventory_message_to_kafka() {
        avroRideRequestProducer.sendRideRequest(new ProductMessage("Osprey", "Backpack", 12.0, 120L));
        ConsumerRecord<String, AvroProduct> singleRecord = KafkaTestUtils.getSingleRecord(ordersConsumer, Constants.INVENTORY);
        assertThat(singleRecord).isNotNull();
    }
}