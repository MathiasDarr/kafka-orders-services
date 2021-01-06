package org.mddarr.ridereceiver.beans;

import org.mddarr.orders.event.dto.FirstOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class QueueConfiguration {

    @Bean
    BlockingQueue<FirstOrder> getOrderRequestBlockingQueue(){
        return new LinkedBlockingQueue<>();
    }

}
