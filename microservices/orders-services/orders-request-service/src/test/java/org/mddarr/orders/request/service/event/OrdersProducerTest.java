package org.mddarr.orders.request.service.event;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.mddarr.orders.event.dto.AvroOrder;
import org.mddarr.orders.request.service.Constants;
import org.mddarr.orders.request.service.UatAbstractTest;

import org.mddarr.orders.request.service.models.OrderRequest;
import org.mddarr.orders.request.service.services.AvroOrderRequestProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrdersProducerTest extends UatAbstractTest {

    @Autowired
    private AvroOrderRequestProducer avroRideRequestProducer;

    @Test
    public void should_send_order_request_to_kafka() {
        List<String> products = new ArrayList(Arrays.asList("BackPack", "Jacket"));
        List<String> vendors = new ArrayList(Arrays.asList("Osprey", "North Face"));
        List<Long> quantities = new ArrayList<>(Arrays.asList(1L, 2L));
        avroRideRequestProducer.sendRideRequest(new OrderRequest("customer1", vendors, products, quantities));
        ConsumerRecord<String, AvroOrder> singleRecord = KafkaTestUtils.getSingleRecord(ordersConsumer, Constants.ORDER_TOPIC);
        assertThat(singleRecord).isNotNull();
    }
}