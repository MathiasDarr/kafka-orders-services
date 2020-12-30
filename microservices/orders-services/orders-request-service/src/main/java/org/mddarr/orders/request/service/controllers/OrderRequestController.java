package org.mddarr.orders.request.service.controllers;



import org.mddarr.orders.request.service.models.OrderRequest;
import org.mddarr.orders.request.service.services.AvroOrderRequestProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderRequestController {

    @Autowired
    AvroOrderRequestProducer avroRideRequestProducer;

    @PutMapping("orders/requests")
    public String postOrdersRequest(@RequestBody OrderRequest orderRequest){
        avroRideRequestProducer.sendRideRequest(orderRequest);
        return "dfd";
    }

}
