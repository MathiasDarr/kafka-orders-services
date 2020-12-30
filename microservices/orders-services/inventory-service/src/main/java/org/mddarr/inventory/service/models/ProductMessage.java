package org.mddarr.inventory.service.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductMessage {
    String vendor;
    String product;
    Double price;
    Long inventory;

}