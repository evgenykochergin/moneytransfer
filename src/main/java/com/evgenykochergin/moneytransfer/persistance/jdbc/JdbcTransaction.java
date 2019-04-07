package com.evgenykochergin.moneytransfer.persistance.jdbc;

import com.evgenykochergin.moneytransfer.persistance.exception.TransactionException;
import com.evgenykochergin.moneytransfer.persistance.ConnectionHolder;
import com.evgenykochergin.moneytransfer.persistance.Transaction;
import com.evgenykochergin.moneytransfer.persistance.TransactionCallback;

import java.sql.Connection;

public class JdbcTransaction<T> implements Transaction<T> {
    private final ConnectionHolder connectionHolder;
    private final TransactionCallback<T> transactionCallback;

    public JdbcTransaction(ConnectionHolder connectionHolder, TransactionCallback<T> transactionCallback) {
        this.connectionHolder = connectionHolder;
        this.transactionCallback = transactionCallback;
    }

    @Override
    public T execute() {
        Connection connection = connectionHolder.getConnection();
        try {
            try {
                connection.setAutoCommit(false);
                T result = transactionCallback.call();
                connection.commit();
                return result;
            } catch (Exception e) {
                connection.rollback();
                throw e;
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new TransactionException("Error in transaction", e);
        } finally {
            connectionHolder.closeConnection();
        }
    }
}

