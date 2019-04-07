package com.evgenykochergin.moneytransfer.persistance;

import com.evgenykochergin.moneytransfer.persistance.jdbc.JdbcTransaction;

import javax.inject.Inject;

public class TransactionManagement {

    private final ConnectionHolder connectionHolder;

    @Inject
    public TransactionManagement(ConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
    }

    public <T> T doInTransaction(TransactionCallback<T> transactionCallback) {
        return new JdbcTransaction<>(connectionHolder, transactionCallback).execute();
    }
}
