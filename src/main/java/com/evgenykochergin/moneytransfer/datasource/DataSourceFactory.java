package com.evgenykochergin.moneytransfer.datasource;

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
        config.setJdbcUrl("jdbc:h2:mem:db;INIT=RUNSCRIPT FROM 'classpath:scripts/create-scheme.sql'");
        config.setUsername("sa");
        config.setPassword("");
        return new HikariDataSource(config);
    }
}
