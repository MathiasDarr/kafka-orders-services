package sample.producer1.controllers;


import org.mddarr.orders.event.dto.AvroOrder;
import org.mddarr.orders.event.dto.FirstOrder;
import org.mddarr.orders.event.dto.OrderState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sample.producer1.models.OrderRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@RestController
public class OrderRequestController {

    @Autowired
    BlockingQueue<AvroOrder> unbounded = new LinkedBlockingQueue<>();

    @PutMapping(value = "/orders")
    public String sendMessage() {
        List<String> vendors = new ArrayList(Arrays.asList("vendor1"));
        List<String> products = new ArrayList(Arrays.asList("product1"));
        List<Long> quantities = new ArrayList(Arrays.asList(1L));

        AvroOrder avroOrder = AvroOrder.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setState(OrderState.PENDING)
                .setQuantites(quantities)
                .setVendors(vendors)
                .setProducts(products)
                .setCustomerId("Charles Goodwin")
                .build();

        unbounded.offer(avroOrder);
        return "order has been posted.. ";
    }

}
