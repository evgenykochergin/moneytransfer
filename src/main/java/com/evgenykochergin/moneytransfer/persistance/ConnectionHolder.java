package com.evgenykochergin.moneytransfer.persistance;

import java.sql.Connection;

public interface ConnectionHolder {

    Connection getConnection();

    void closeConnection();
}
