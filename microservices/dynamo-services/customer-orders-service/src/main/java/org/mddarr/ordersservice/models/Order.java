package org.mddarr.ordersservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    String orderID;
    long creationDate;
    String customerID;
    List<String> vendors;
    List<String> products;
    List<Long> quantities;
//    List<Double> prices;
    String order_status;



}
