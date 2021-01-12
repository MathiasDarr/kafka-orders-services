package org.mddarr.ordersviews.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    public enum Type {
        DEPOSIT, WITHDRAW
    }

    String guid;
    String account;
    BigDecimal amount;
    Type type;
    String currency;
    String country;
}