package com.evgenykochergin.moneytransfer.persistance.exception;

public class ConnectionHolderCloseConnectionException extends IllegalStateException {
    public ConnectionHolderCloseConnectionException(String message) {
        super(message);
    }
}
