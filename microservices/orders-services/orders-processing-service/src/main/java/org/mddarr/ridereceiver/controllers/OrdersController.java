package org.mddarr.ridereceiver.controllers;


import org.mddarr.orders.event.dto.AvroOrder;
import org.mddarr.orders.event.dto.FirstOrder;
import org.mddarr.orders.event.dto.OrderState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

@RestController
public class OrdersController {


    @Autowired
    BlockingQueue<FirstOrder> orderBlockingQueue;
//
    @PostMapping(value = "postOrder")
    public String sendMessage() {
        List<String> vendors = new ArrayList(Arrays.asList("vendor1"));
        List<String> products = new ArrayList(Arrays.asList("product1"));
        List<Long> quantities = new ArrayList(Arrays.asList(1L));


        AvroOrder avroOrder = AvroOrder.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setState(OrderState.PENDING)
                .setVendors(vendors)
                .setQuantites(quantities)
                .setProducts(products)
                .setCustomerId("Charles Goodwin")
                .build();
        FirstOrder firstOrder = FirstOrder.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setCustomerId("Charles Goodwin")
                .build();
        orderBlockingQueue.offer(firstOrder);
        System.out.println("THE ORDER REQUEST HAS BEEN SENT " + firstOrder);
        return "Order sent";
    }

}
