package org.mddarr.driversservice.services;

import org.mddarr.driversservice.Constants;
import org.mddarr.rides.event.dto.AvroDriver;
import org.mddarr.rides.event.dto.AvroRideRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AvroDriverProducer implements AvroDriverInterface {

    @Autowired
    private KafkaTemplate<String, AvroDriver> kafkaTemplateEvent1;

    private static final Logger logger = LoggerFactory.getLogger(AvroDriverProducer.class);

    public void postKafkaDriver() {
        AvroDriver ride = AvroDriver.newBuilder().setDriverid("driver1").setFirstname("Jod").setLastname("Beab").build();
        logger.info("Send event 1 {}", ride);
        kafkaTemplateEvent1.send(Constants.DRIVERS_TOPIC, ride);
    }

}
