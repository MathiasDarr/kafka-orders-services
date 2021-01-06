package sample.producer1.controllers;


import org.mddarr.orders.event.dto.FirstOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@RestController
public class OrderRequestController {

    @Autowired
    BlockingQueue<FirstOrder> unbounded = new LinkedBlockingQueue<>();

    @RequestMapping(value = "/messages", method = RequestMethod.POST)
    public String sendMessage() {
        FirstOrder firstOrder = FirstOrder.newBuilder()
                .setCustomerId("Charles")
                .setId("dier")
                .build();
        unbounded.offer(firstOrder);
        return "ok, have fun with v1 payload!";
    }

}
