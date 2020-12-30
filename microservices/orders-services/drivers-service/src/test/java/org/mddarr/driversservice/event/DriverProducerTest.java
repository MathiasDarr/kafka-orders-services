package org.mddarr.driversservice.event;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.mddarr.driversservice.Constants;
import org.mddarr.driversservice.UatAbstractTest;
import org.mddarr.driversservice.services.AvroDriverProducer;
import org.mddarr.rides.event.dto.AvroDriver;
import org.mddarr.rides.event.dto.AvroRideRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

public class DriverProducerTest extends UatAbstractTest {

    @Autowired
    private AvroDriverProducer avroRideRequestProducer;

    @Test
    public void should_produce_driver_kafka_message() {
        AvroDriver driver = new AvroDriver("driverID1","Charles","adf");
        avroRideRequestProducer.postKafkaDriver();
        ConsumerRecord<String, AvroDriver> singleRecord = KafkaTestUtils.getSingleRecord(avroDriverConsumer, Constants.DRIVERS_TOPIC);
        assertThat(singleRecord).isNotNull();
    }
}