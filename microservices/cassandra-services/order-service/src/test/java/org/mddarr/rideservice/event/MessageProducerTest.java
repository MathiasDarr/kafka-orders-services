package org.mddarr.rideservice.event;

import org.mddarr.rideservice.Constants;
import org.mddarr.rideservice.UatAbstractTest;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.mddarr.rides.event.dto.AvroRideRequest;

import org.mddarr.rideservice.models.requests.PostRideRequest;
import org.mddarr.rideservice.services.AvroRideRequestProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageProducerTest extends UatAbstractTest {

    @Autowired
    private AvroRideRequestProducer avroRideRequestProducer;

    @Test
    public void should_send_ride_request() {
        avroRideRequestProducer.sendRideRequest(new PostRideRequest("Charles",6));
        ConsumerRecord<String, AvroRideRequest> singleRecord = KafkaTestUtils.getSingleRecord(event1Consumer, Constants.Rides_TOPIC);
        assertThat(singleRecord).isNotNull();
    }


}