package com.evgenykochergin.moneytransfer.model.exception;

import java.util.UUID;

public class AccountWithdrawNotEnoughAmountException extends RuntimeException {


    public AccountWithdrawNotEnoughAmountException(UUID id, Throwable cause) {
        super("Not enough amount to withdraw for account with id " + id, cause);
    }
}
