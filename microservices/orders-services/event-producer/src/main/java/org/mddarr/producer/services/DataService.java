package org.mddarr.producer.services;

import org.mddarr.rides.event.dto.AvroDriver;
import org.mddarr.rides.event.dto.AvroRideRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataService {

    public static List<AvroDriver> getProductsFromDB(){
        List<AvroDriver> avroDrivers = new ArrayList<>();
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgresdb",
                "postgres", "postgres");
             PreparedStatement pst = con.prepareStatement("SELECT driver_id, first_name, last_name FROM drivers");
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                avroDrivers.add(new AvroDriver (rs.getString(1),rs.getString(2),rs.getString(3) ));
            }
        } catch (SQLException e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return avroDrivers;
    }

    public static List<AvroRideRequest> getRideRequestsFromDB(){
        List<AvroRideRequest> avroRideRequests = new ArrayList<>();
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgresdb",
                "postgres", "postgres");
             PreparedStatement pst = con.prepareStatement("SELECT request_id, userid,riders FROM ride_requests");
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                avroRideRequests.add(new AvroRideRequest (rs.getString(1),rs.getString(2),rs.getInt(3) ));
            }
        } catch (SQLException e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return avroRideRequests;
    }


}
