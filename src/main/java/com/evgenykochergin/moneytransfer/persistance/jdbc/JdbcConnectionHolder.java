package com.evgenykochergin.moneytransfer.persistance.jdbc;

import com.evgenykochergin.moneytransfer.exception.ApplicationException;
import com.evgenykochergin.moneytransfer.persistance.ConnectionHolder;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JdbcConnectionHolder implements ConnectionHolder {
    private final DataSource dataSource;
    private final ThreadLocal<Connection> holder;

    @Inject
    public JdbcConnectionHolder(DataSource dataSource) {
        this.dataSource = dataSource;
        this.holder = new ThreadLocal<>();
    }

    @Override
    public Connection getConnection() {
        if (holder.get() == null) {
            try {
                holder.set(dataSource.getConnection());
            } catch (SQLException e) {
                throw new ApplicationException("Cannot get connection", e);
            }
        }
        return holder.get();
    }

    @Override
    public void closeConnection() {
        Connection connection = holder.get();
        if (connection == null) return;
        try {
            connection.close();
        } catch (SQLException e) {
            throw new ApplicationException("Cannot close connection", e);
        } finally {
            holder.set(null);
        }
    }
}
