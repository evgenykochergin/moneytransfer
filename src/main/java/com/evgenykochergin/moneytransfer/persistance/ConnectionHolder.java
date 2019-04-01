package com.evgenykochergin.moneytransfer.persistance;

import com.evgenykochergin.moneytransfer.exception.ApplicationException;
import com.evgenykochergin.moneytransfer.persistance.exception.ConnectionHolderCloseConnection;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionHolder {
    private final DataSource dataSource;
    private final ThreadLocal<Connection> holder;

    @Inject
    public ConnectionHolder(DataSource dataSource) {
        this.dataSource = dataSource;
        this.holder = new ThreadLocal<>();
    }

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

    public void closeConnection() {
        Connection connection = holder.get();
        if (connection == null) throw new ConnectionHolderCloseConnection("Cannot close not opened connection");
        try {
            connection.close();
        } catch (SQLException e) {
            throw new ApplicationException("Cannot close connection", e);
        } finally {
            holder.set(null);
        }
    }
}
