package org.mddarr.driversservice.controllers;


import org.mddarr.driversservice.models.Driver;
import org.mddarr.driversservice.models.requests.PostDriverRequest;
import org.mddarr.driversservice.services.AvroDriverProducer;
import org.mddarr.driversservice.services.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class DriversController {

    @Autowired
    private AvroDriverProducer avroDriverProducer;

    private final DriverService driverService;

    public DriversController(DriverService driverService){
        this.driverService = driverService;
    }

    @GetMapping(value ="drivers")
    public List<Driver> getDrivers(){
        return driverService.getDrivers();
    }

    @GetMapping(value="drivers/{driver_id}")
    public Optional<Driver> getPatientDetail(@PathVariable String patient_id){
        return driverService.getPatientDetail(patient_id);
    }

    @PutMapping(value="drivers")
    public String createPatient(@RequestBody PostDriverRequest postDriverRequest){
        return driverService.postDriver(postDriverRequest);
    }

    @PutMapping(value="drivers/active")
    public void activateDriver(){
        avroDriverProducer.postKafkaDriver();
    }
}
