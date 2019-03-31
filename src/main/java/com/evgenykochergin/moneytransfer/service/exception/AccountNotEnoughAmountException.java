package com.evgenykochergin.moneytransfer.service.exception;

import java.util.UUID;

public class AccountNotEnoughAmountException extends RuntimeException {
    public AccountNotEnoughAmountException(UUID id) {
        super("Not enough amount for account with id " + id);
    }
}
