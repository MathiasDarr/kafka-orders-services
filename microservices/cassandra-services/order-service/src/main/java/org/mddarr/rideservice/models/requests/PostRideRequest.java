package org.mddarr.rideservice.models.requests;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class PostRideRequest {
    private String userid;
    private Integer riders;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Integer getRiders() {
        return riders;
    }

    public void setRiders(Integer riders) {
        this.riders = riders;
    }
}
