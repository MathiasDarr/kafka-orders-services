package org.mddarr.driversservice.services;



import org.mddarr.driversservice.models.Driver;
import org.mddarr.driversservice.models.requests.PostDriverRequest;

import java.util.List;
import java.util.Optional;

public interface DriverServiceInterface {

    List<Driver> getDrivers();
    Optional<Driver> getPatientDetail(String id);
    String postDriver(PostDriverRequest postPatientRequest);
}