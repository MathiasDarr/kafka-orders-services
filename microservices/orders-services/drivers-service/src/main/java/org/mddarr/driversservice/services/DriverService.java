package org.mddarr.driversservice.services;



import org.mddarr.driversservice.models.Driver;
import org.mddarr.driversservice.models.requests.PostDriverRequest;
import org.mddarr.driversservice.repositories.DriverRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DriverService implements DriverServiceInterface {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository){
        this.driverRepository = driverRepository;
    }

    @Override
    public List<Driver> getDrivers() {
        return driverRepository.findAll();
    }

    @Override
    public Optional<Driver> getPatientDetail(String id){
        return driverRepository.findById(id);
    }

    @Override
    public String postDriver(PostDriverRequest postPatientRequest) {
        Driver driver = new Driver();
        String driver_id = UUID.randomUUID().toString();
        driver.setDriver_id(driver_id);
        driver.setLast_name(postPatientRequest.getLast_name());
        driver.setFirst_name(postPatientRequest.getFirst_name());
        driver.setPassword("1!ZionTF");
        driverRepository.save(driver);
        return driver_id;
    }
}
