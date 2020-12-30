package org.mddarr.ordersservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    String customerID;
    List<String> vendors;
    List<String> products;
    List<Long> quantities;

}