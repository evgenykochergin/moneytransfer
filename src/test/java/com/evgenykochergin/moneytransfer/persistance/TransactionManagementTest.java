package com.evgenykochergin.moneytransfer.persistance;

import com.evgenykochergin.moneytransfer.datasource.DataSourceFactory;
import com.evgenykochergin.moneytransfer.persistance.exception.TransactionException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static junit.framework.TestCase.assertSame;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.Matchers.hasProperty;

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
        exception.expect(TransactionException.class);
        exception.expectMessage(containsString("Error in transaction"));
        exception.expectCause(allOf(
                isA(RuntimeException.class),
                hasProperty("message", is("Cause message"))
        ));
        transactionManagement.doInTransaction(() -> {
            assertTrue(connectionHolder.hasConnection());
            throw new RuntimeException("Cause message");
        });
        assertTrue(!connectionHolder.hasConnection());
    }
}