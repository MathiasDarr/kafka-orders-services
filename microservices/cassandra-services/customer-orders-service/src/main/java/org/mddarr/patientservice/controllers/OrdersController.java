package org.mddarr.patientservice.controllers;

import org.mddarr.patientservice.models.Order;
import org.mddarr.patientservice.services.OrdersService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrdersController {

    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService){
        this.ordersService = ordersService;
    }

    @GetMapping(value="orders")
    public List<Order> getPatients(){
        return ordersService.getOrders();
    }


}
