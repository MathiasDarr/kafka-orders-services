package org.mddarr.ordersviews.models;

import lombok.Value;

@Value
public class TransactionResult {

    public enum ErrorType {
        INSUFFICIENT_FUNDS
    }

    Transaction transaction;
    boolean success;
    ErrorType errorType;
}
