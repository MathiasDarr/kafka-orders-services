package org.mddarr.producer.runnable;


import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.joda.time.DateTime;
import org.mddarr.producer.Constants;
import org.mddarr.producer.kafka.templates.KafkaGenericTemplate;
import org.mddarr.products.AvroProduct;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;


public class PurchaseEventProducerThread implements Runnable {
    private final Log log = LogFactory.getLog(getClass());

    private final CountDownLatch latch;

    private int recordCount;

    private KafkaTemplate<String, AvroProduct> purchaseEventKafkaTemplate;


    public PurchaseEventProducerThread(CountDownLatch latch){
        KafkaGenericTemplate<AvroProduct> kafkaGenericTemplate = new KafkaGenericTemplate<>();
        purchaseEventKafkaTemplate = kafkaGenericTemplate.getKafkaTemplate();
        purchaseEventKafkaTemplate.setDefaultTopic(Constants.PURCHASE_EVENT_TOPIC);
        this.latch = latch;
    }

    public void run() {

        int tweetCount = 0;

        while(latch.getCount() >0 ) {
            try {
                System.out.println("IAM DFD");
                Thread.sleep(1000);
                    tweetCount +=1;
            } catch (Exception e) {

            }
        }
        close();
    }

    public void close(){
            log.info("Closing Purchase Event Producer");
            latch.countDown();
    }
}
