package com.evgenykochergin.moneytransfer.service.impl;

import com.evgenykochergin.moneytransfer.datasource.DataSourceFactory;
import com.evgenykochergin.moneytransfer.model.Account;
import com.evgenykochergin.moneytransfer.model.Amount;
import com.evgenykochergin.moneytransfer.persistance.ConnectionHolder;
import com.evgenykochergin.moneytransfer.persistance.TransactionManagement;
import com.evgenykochergin.moneytransfer.persistance.jdbc.JdbcTransactionalFactory;
import com.evgenykochergin.moneytransfer.repository.AccountRepository;
import com.evgenykochergin.moneytransfer.repository.impl.JdbcAccountRepository;
import com.evgenykochergin.moneytransfer.service.AccountService;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.UUID;

import static junit.framework.TestCase.assertSame;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

public class AccountServiceImplTest {

    private AccountService accountService;

    @Before
    public void setUp() {
        ConnectionHolder connectionHolder = new ConnectionHolder(DataSourceFactory.createDataSource());
        JdbcTransactionalFactory jdbcTransactionalFactory = new JdbcTransactionalFactory(connectionHolder);
        AccountRepository accountRepository = new JdbcAccountRepository(jdbcTransactionalFactory);
        TransactionManagement transactionManagement = new TransactionManagement(connectionHolder);
        accountService = new AccountServiceImpl(transactionManagement, accountRepository);
    }

    @Test
    public void canAddAndGetAccount() {
        Account accountToCreate = createAccount(100);
        Account addAccount = accountService.add(accountToCreate);
        Account getAccount = accountService.get(accountToCreate.getId());
        assertThat(addAccount, equalTo(getAccount));
    }

    @Test
    public void canGetAllAccounts() {
        Account accountToCreate1 = createAccount(1);
        Account accountToCreate2 = createAccount(2);
        accountService.add(accountToCreate1);
        accountService.add(accountToCreate2);
        Collection<Account> accounts = accountService.getAll();
        assertThat(accounts, containsInAnyOrder(accountToCreate1, accountToCreate2));
        assertSame(2, accounts.size());
    }

    @Test
    public void canUpdateAccount() {
        Account accountToCreate = createAccount(100);
        accountService.add(accountToCreate);
        Account accountToUpdate = accountToCreate
                .toBuilder()
                .amount(Amount.of(new BigDecimal(50)))
                .build();
        accountService.update(accountToUpdate);
        Account getAccount = accountService.get(accountToCreate.getId());
        assertThat(getAccount.getVersion(), equalTo(accountToUpdate.getVersion() + 1));
    }


    private Account createAccount(int amountValue) {
        return Account.builder()
                .id(UUID.randomUUID())
                .amount(Amount.of(new BigDecimal(amountValue)))
                .version(0)
                .build();
    }
}