package com.evgenykochergin.moneytransfer.model;


import com.evgenykochergin.moneytransfer.model.exception.AccountDepositZeroAmountException;
import com.evgenykochergin.moneytransfer.model.exception.AccountWithdrawNotEnoughAmountException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class AccountTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private final Account testAccount = Account.builder()
            .id(UUID.randomUUID())
            .amount(Amount.of(new BigDecimal(10)))
            .build();

    @Test
    public void deposit() {
        Account depositedAccount = testAccount.deposit(Amount.of(new BigDecimal(1)));
        assertThat(depositedAccount.getAmount(), equalTo(Amount.of(new BigDecimal(11))));
    }

    @Test
    public void depositZero() {
        exception.expect(AccountDepositZeroAmountException.class);
        exception.expectMessage(containsString(
                "Cannot deposit zero amount for account with id " + testAccount.getId()));
        Account depositedAccount = testAccount.deposit(Amount.of(BigDecimal.ZERO));
    }

    @Test
    public void withdraw() {
        Account withdrawnAccount = testAccount.withdraw(Amount.of(new BigDecimal(1)));
        assertThat(withdrawnAccount.getAmount(), equalTo(Amount.of(new BigDecimal(9))));
    }

    @Test
    public void withdrawAll() {
        Account withdrawnAccount = testAccount.withdraw(Amount.of(new BigDecimal(10)));
        assertThat(withdrawnAccount.getAmount(), equalTo(Amount.of(BigDecimal.ZERO)));
    }

    @Test
    public void accountWithdrawException() {
        exception.expect(AccountWithdrawNotEnoughAmountException.class);
        exception.expectMessage(containsString(
                "Not enough amount to withdraw for account with id " + testAccount.getId()));
        Account withdrawnAccount = testAccount.withdraw(Amount.of(new BigDecimal(20)));
        assertThat(withdrawnAccount.getAmount(), equalTo(Amount.of(BigDecimal.ZERO)));
    }


}