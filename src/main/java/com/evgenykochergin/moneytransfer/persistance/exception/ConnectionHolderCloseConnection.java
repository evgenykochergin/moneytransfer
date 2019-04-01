package com.evgenykochergin.moneytransfer.persistance.exception;

public class ConnectionHolderCloseConnection extends IllegalStateException {
    public ConnectionHolderCloseConnection(String message) {
        super(message);
    }
}
