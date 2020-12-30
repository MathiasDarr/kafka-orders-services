package org.mddarr.rideservice.controllers;



import org.mddarr.rideservice.models.requests.PostRideRequest;
import org.mddarr.rideservice.services.AvroRideRequestProducer;
import org.mddarr.rideservice.services.RideRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RideRequestController {

    @Autowired
    AvroRideRequestProducer avroRideRequestProducer;

    @Autowired
    RideRequestService rideRequestService;

    @PutMapping
    public String postRideRequest(@RequestBody PostRideRequest rideRequest){
        String request_id = rideRequestService.postRideRequest(rideRequest);
        avroRideRequestProducer.sendRideRequest(rideRequest);
        return request_id;
    }

}
