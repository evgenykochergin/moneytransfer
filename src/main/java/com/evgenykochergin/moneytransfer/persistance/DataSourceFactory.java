package com.evgenykochergin.moneytransfer.persistance;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DataSourceFactory {

    /**
     * Creates a thread-safe datasource with connection pooling
     *
     * @return a datasource
     */
    public static DataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:hsqldb:mem:mymemdb");
        config.setUsername("SA");
        config.setPassword("");
        return new HikariDataSource(config);
    }
}
