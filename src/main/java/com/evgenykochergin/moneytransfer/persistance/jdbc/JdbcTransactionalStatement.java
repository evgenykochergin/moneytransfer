package com.evgenykochergin.moneytransfer.persistance.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface JdbcTransactionalStatement<T> {

    T getResult(PreparedStatement statement) throws SQLException;
}