package com.evgenykochergin.moneytransfer.guice.module;

import com.evgenykochergin.moneytransfer.persistance.ConnectionHolder;
import com.evgenykochergin.moneytransfer.datasource.DataSourceFactory;
import com.evgenykochergin.moneytransfer.persistance.TransactionManagement;
import com.evgenykochergin.moneytransfer.persistance.jdbc.JdbcTransactionalFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import javax.sql.DataSource;

public class CoreModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DataSource.class).toInstance(DataSourceFactory.createDataSource());
        bind(ConnectionHolder.class).in(Singleton.class);
        bind(JdbcTransactionalFactory.class).in(Singleton.class);
        bind(TransactionManagement.class).in(Singleton.class);
    }
}
