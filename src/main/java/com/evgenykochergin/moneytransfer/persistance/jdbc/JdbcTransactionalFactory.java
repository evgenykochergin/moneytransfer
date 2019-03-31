package com.evgenykochergin.moneytransfer.persistance.jdbc;

import com.evgenykochergin.moneytransfer.persistance.ConnectionHolder;

import javax.inject.Inject;

public class JdbcTransactionalFactory {
    private final ConnectionHolder connectionHolder;

    @Inject
    public JdbcTransactionalFactory(ConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
    }

    public <T> JdbcTransactional<T> create(String query, JdbcTransactionalStatement<T> statement) {
        return new JdbcTransactional<>(connectionHolder, query, statement);
    }
}
