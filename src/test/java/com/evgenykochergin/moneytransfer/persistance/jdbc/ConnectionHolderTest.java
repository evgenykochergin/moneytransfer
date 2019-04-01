package com.evgenykochergin.moneytransfer.persistance.jdbc;


import com.evgenykochergin.moneytransfer.datasource.DataSourceFactory;
import com.evgenykochergin.moneytransfer.persistance.ConnectionHolder;
import com.evgenykochergin.moneytransfer.persistance.exception.ConnectionHolderCloseConnection;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.Connection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

public class ConnectionHolderTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private ConnectionHolder connectionHolder;

    @Before
    public void setUp() {
        this.connectionHolder = new ConnectionHolder(DataSourceFactory.createDataSource());
    }

    @Test
    public void connectionIsNotNull() {
        assertNotNull(this.connectionHolder.getConnection());
    }

    @Test
    public void cannotCloseNotOpenedConnection() {
        exception.expect(ConnectionHolderCloseConnection.class);
        exception.expectMessage(containsString("Cannot close not opened connection"));
        this.connectionHolder.closeConnection();
    }

    @Test
    public void givesTheSameConnectionInTheSameThread() {
        assertThat(connectionHolder.getConnection(), equalTo(connectionHolder.getConnection()));
    }

    @Test
    public void afterCloseGetsNewConnection() {
        Connection before = connectionHolder.getConnection();
        connectionHolder.closeConnection();
        Connection after = connectionHolder.getConnection();
        assertThat(before, not(equalTo(after)));
    }

    @Test
    public void givesDifferentConnectionsInDifferentThreads() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Connection> submit1 = executorService.submit(() -> connectionHolder.getConnection());
        Future<Connection> submit2 = executorService.submit(() -> connectionHolder.getConnection());
        assertThat(submit1.get(), not(equalTo(submit2.get())));
    }
}