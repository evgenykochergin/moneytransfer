package com.evgenykochergin.moneytransfer.model.exception;

public class NegativeAmountException extends IllegalArgumentException {
    public NegativeAmountException(String message) {
        super(message);
    }
}
