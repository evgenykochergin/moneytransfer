package com.evgenykochergin.moneytransfer.model.exception;

import java.util.UUID;

public class AccountDepositZeroAmountException extends RuntimeException {

    public AccountDepositZeroAmountException(UUID id) {
        super("Cannot deposit zero amount for account with id " + id);
    }
}
