package com.evgenykochergin.moneytransfer.persistance.exception;

public class TransactionOptimisticLockException extends TransactionException {

    public TransactionOptimisticLockException(String message, Throwable cause) {
        super(message, cause);
    }
}
