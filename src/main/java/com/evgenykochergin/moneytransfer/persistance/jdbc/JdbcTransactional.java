package com.evgenykochergin.moneytransfer.persistance.jdbc;

import com.evgenykochergin.moneytransfer.exception.ApplicationException;
import com.evgenykochergin.moneytransfer.persistance.ConnectionHolder;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcTransactional<T> {

    private final ConnectionHolder connectionHolder;
    private final String query;
    private final JdbcTransactionalStatement<T> statement;

    public JdbcTransactional(ConnectionHolder connectionHolder,
                             String query,
                             JdbcTransactionalStatement<T> statement) {
        this.connectionHolder = connectionHolder;
        this.query = query;
        this.statement = statement;
    }

    public T execute() {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement(query)) {
            return statement.getResult(preparedStatement);
        } catch (SQLException e) {
            throw new ApplicationException("Cant execute query: " + query, e);
        }
    }
}