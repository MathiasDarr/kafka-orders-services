package org.mddarr.rideservice.repository;

import org.mddarr.rideservice.models.entities.RideRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RideRequestRepostiory extends JpaRepository<RideRequest, String> {

}
