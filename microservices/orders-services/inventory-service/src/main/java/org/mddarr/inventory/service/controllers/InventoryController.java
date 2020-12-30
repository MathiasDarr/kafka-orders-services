package org.mddarr.inventory.service.controllers;



import org.mddarr.inventory.service.models.ProductMessage;
import org.mddarr.inventory.service.services.AvroOrderRequestProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InventoryController {

    @Autowired
    AvroOrderRequestProducer avroRideRequestProducer;

    @PutMapping("inventory/post")
    public String postOrdersRequest(@RequestBody ProductMessage productMessage){
        avroRideRequestProducer.sendRideRequest(productMessage);
        return "dfd";
    }

}
