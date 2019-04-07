package com.evgenykochergin.moneytransfer.persistance;

import com.evgenykochergin.moneytransfer.datasource.DataSourceFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static junit.framework.TestCase.assertSame;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.containsString;

public class TransactionManagementTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private ConnectionHolder connectionHolder;
    private TransactionManagement transactionManagement;

    @Before
    public void setUp() {
        connectionHolder = new ConnectionHolder(DataSourceFactory.createDataSource());
        transactionManagement = new TransactionManagement(connectionHolder);
    }

    @Test
    public void canGetResultAfterCompletedTransaction() {
        String result = transactionManagement.doInTransaction(() -> "result");
        assertSame("result", result);
        assertTrue(!connectionHolder.hasConnection());
    }

    @Test
    public void cannotGetResultAfterUncompletedTransaction() {
        exception.expect(RuntimeException.class);
        exception.expectMessage(containsString("Cause message"));
        transactionManagement.doInTransaction(() -> {
            assertTrue(connectionHolder.hasConnection());
            throw new RuntimeException("Cause message");
        });
        assertTrue(!connectionHolder.hasConnection());
    }
}