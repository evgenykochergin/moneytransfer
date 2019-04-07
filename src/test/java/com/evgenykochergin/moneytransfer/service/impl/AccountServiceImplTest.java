package com.evgenykochergin.moneytransfer.service.impl;

import com.evgenykochergin.moneytransfer.datasource.DataSourceFactory;
import com.evgenykochergin.moneytransfer.model.Account;
import com.evgenykochergin.moneytransfer.model.Amount;
import com.evgenykochergin.moneytransfer.model.exception.AccountDepositZeroAmountException;
import com.evgenykochergin.moneytransfer.model.exception.AccountWithdrawNotEnoughAmountException;
import com.evgenykochergin.moneytransfer.persistance.ConnectionHolder;
import com.evgenykochergin.moneytransfer.persistance.TransactionManagement;
import com.evgenykochergin.moneytransfer.persistance.exception.TransactionException;
import com.evgenykochergin.moneytransfer.persistance.exception.TransactionOptimisticLockException;
import com.evgenykochergin.moneytransfer.persistance.jdbc.JdbcTransactionalFactory;
import com.evgenykochergin.moneytransfer.repository.AccountRepository;
import com.evgenykochergin.moneytransfer.repository.impl.JdbcAccountRepository;
import com.evgenykochergin.moneytransfer.service.AccountService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertSame;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.fail;

public class AccountServiceImplTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

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

    @Test
    public void canRemoveAccounts() {
        Account accountToCreate1 = createAccount(1);
        Account accountToCreate2 = createAccount(2);

        accountService.add(accountToCreate1);
        accountService.add(accountToCreate2);
        Collection<Account> accountsAfterAdd = accountService.getAll();
        assertThat(accountsAfterAdd, containsInAnyOrder(accountToCreate1, accountToCreate2));
        assertSame(2, accountsAfterAdd.size());

        accountService.remove(accountToCreate1.getId());
        Collection<Account> accountsAfterRemove = accountService.getAll();
        assertThat(accountsAfterRemove, containsInAnyOrder(accountToCreate2));
        assertSame(1, accountsAfterRemove.size());
    }

    @Test
    public void canTransfer() {
        Account accountToCreate1 = createAccount(1000);
        Account accountToCreate2 = createAccount(500);

        accountService.add(accountToCreate1);
        accountService.add(accountToCreate2);

        accountService.transfer(
                accountToCreate1.getId(),
                accountToCreate2.getId(),
                Amount.of(new BigDecimal(200)));

        assertThat(accountService.get(accountToCreate1.getId()).getAmount(),
                equalTo(Amount.of(new BigDecimal(800))));
        assertThat(accountService.get(accountToCreate2.getId()).getAmount(),
                equalTo(Amount.of(new BigDecimal(700))));
    }

    @Test
    public void cannotTransferWithExceededAmount() {
        Account accountToCreate1 = createAccount(1000);
        Account accountToCreate2 = createAccount(500);

        exception.expect(TransactionException.class);
        exception.expectMessage(containsString("Error in transaction"));
        exception.expectCause(allOf(
                isA(AccountWithdrawNotEnoughAmountException.class),
                hasProperty("message",
                        is("Not enough amount to withdraw for account with id " + accountToCreate1.getId()))
        ));


        accountService.add(accountToCreate1);
        accountService.add(accountToCreate2);

        accountService.transfer(
                accountToCreate1.getId(),
                accountToCreate2.getId(),
                Amount.of(new BigDecimal(2000)));

    }

    @Test
    public void cannotTransferZeroAmount() {
        Account accountToCreate1 = createAccount(1000);
        Account accountToCreate2 = createAccount(500);

        exception.expect(TransactionException.class);
        exception.expectMessage(containsString("Error in transaction"));
        exception.expectCause(allOf(
                isA(AccountDepositZeroAmountException.class),
                hasProperty("message",
                        is("Cannot deposit zero amount for account with id " + accountToCreate2.getId()))
        ));


        accountService.add(accountToCreate1);
        accountService.add(accountToCreate2);

        accountService.transfer(accountToCreate1.getId(), accountToCreate2.getId(), Amount.of(BigDecimal.ZERO));

    }

    @Test
    public void throwsOptimisticLockOnConcurrentUpdate() throws Exception {
        Account account = createAccount(100);
        accountService.add(account);


        AtomicBoolean optimistiocLockOnUpdate1 = new AtomicBoolean(false);
        AtomicBoolean optimistiocLockOnUpdate2 = new AtomicBoolean(false);

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        Collection<Callable<Account>> tasks = Arrays.asList(
                () -> {
                    Account accountToUpdate1 = account.toBuilder().amount(Amount.of(new BigDecimal(1))).build();
                    try {
                        return accountService.update(accountToUpdate1);
                    } catch (TransactionOptimisticLockException e) {
                        optimistiocLockOnUpdate1.set(true);
                    }
                    return null;
                },
                () -> {
                    Account accountToUpdate2 = account.toBuilder().amount(Amount.of(new BigDecimal(2))).build();
                    try {
                        return accountService.update(accountToUpdate2);
                    } catch (TransactionOptimisticLockException e) {
                        optimistiocLockOnUpdate2.set(true);
                    }
                    return null;
                }
        );
        List<Future<Account>> futures = executorService.invokeAll(tasks);
        Account account1 = futures.get(0).get();
        Account account2 = futures.get(1).get();

        if (!optimistiocLockOnUpdate1.get() && !optimistiocLockOnUpdate2.get()) {
            fail("Optimistic lock did not happen");
        }

        if (optimistiocLockOnUpdate1.get()) {
            assertNull(account1);
            assertNotNull(account2);
            assertThat(account2.getVersion(), equalTo(account.getVersion() + 1));
            assertThat(account2.getAmount(), equalTo(Amount.of(new BigDecimal(2))));
        }

        if (optimistiocLockOnUpdate2.get()) {
            assertNull(account2);
            assertNotNull(account1);
            assertThat(account1.getVersion(), equalTo(account.getVersion() + 1));
            assertThat(account1.getAmount(), equalTo(Amount.of(new BigDecimal(1))));
        }
    }


    @Test
    public void throwsOptimisticLockOnConcurrentTransfer() throws Exception {
        Account accountToCreate1 = createAccount(1000);
        Account accountToCreate2 = createAccount(500);

        accountService.add(accountToCreate1);
        accountService.add(accountToCreate2);

        AtomicBoolean optimistiocLockOnTransfer1 = new AtomicBoolean(false);
        AtomicBoolean optimistiocLockOnTransfer2 = new AtomicBoolean(false);

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        Collection<Callable<Account>> tasks = Arrays.asList(
                () -> {
                    try {
                        accountService.transfer(
                                accountToCreate1.getId(),
                                accountToCreate2.getId(),
                                Amount.of(new BigDecimal(100)));
                    } catch (TransactionOptimisticLockException e) {
                        optimistiocLockOnTransfer1.set(true);
                    }
                    return accountService.get(accountToCreate1.getId());
                },
                () -> {
                    try {
                        accountService.transfer(
                                accountToCreate1.getId(),
                                accountToCreate2.getId(),
                                Amount.of(new BigDecimal(200)));
                    } catch (TransactionOptimisticLockException e) {
                        optimistiocLockOnTransfer2.set(true);
                    }
                    return accountService.get(accountToCreate2.getId());
                }
        );
        List<Future<Account>> futures = executorService.invokeAll(tasks);
        Account account1 = futures.get(0).get();
        Account account2 = futures.get(1).get();

        if (!optimistiocLockOnTransfer1.get() && !optimistiocLockOnTransfer2.get()) {
            fail("Optimistic lock did not happen");
        }

        assertThat(account1.getVersion(), equalTo(accountToCreate1.getVersion() + 1));
        assertThat(account2.getVersion(), equalTo(accountToCreate2.getVersion() + 1));

        if (optimistiocLockOnTransfer1.get()) {
            assertThat(account1.getAmount(), equalTo(Amount.of(new BigDecimal(800))));
            assertThat(account2.getAmount(), equalTo(Amount.of(new BigDecimal(700))));
        }

        if (optimistiocLockOnTransfer2.get()) {
            assertThat(account1.getAmount(), equalTo(Amount.of(new BigDecimal(900))));
            assertThat(account2.getAmount(), equalTo(Amount.of(new BigDecimal(600))));
        }

    }

    private Account createAccount(int amountValue) {
        return Account.builder()
                .id(UUID.randomUUID())
                .amount(Amount.of(new BigDecimal(amountValue)))
                .version(0)
                .build();
    }
}