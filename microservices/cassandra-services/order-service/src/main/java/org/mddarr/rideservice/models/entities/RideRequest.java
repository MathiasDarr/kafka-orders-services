package org.mddarr.rideservice.models.entities;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name ="ride_requests")
@AllArgsConstructor
@NoArgsConstructor
public class RideRequest {

    @Id
    private String request_id;

    @Column
    private Date request_time;

    @Column
    private String userid;

    @Column
    private Integer riders;

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public Date getRequest_time() {
        return request_time;
    }

    public void setRequest_time(Date request_time) {
        this.request_time = request_time;
    }

    public Integer getRiders() {
        return riders;
    }

    public void setRiders(Integer riders) {
        this.riders = riders;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
