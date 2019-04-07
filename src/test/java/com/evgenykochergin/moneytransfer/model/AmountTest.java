package com.evgenykochergin.moneytransfer.model;

import com.evgenykochergin.moneytransfer.model.exception.NegativeAmountException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class AmountTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void add() {
        Amount amount1 = Amount.of(new BigDecimal(10));
        Amount amount2 = Amount.of(new BigDecimal(5));
        Amount amount = amount1.add(amount2);
        assertThat(amount, equalTo(Amount.of(new BigDecimal(15))));
    }

    @Test
    public void subtract() {
        Amount amount1 = Amount.of(new BigDecimal(10));
        Amount amount2 = Amount.of(new BigDecimal(2));
        Amount amount = amount1.subtract(amount2);
        assertThat(amount, equalTo(Amount.of(new BigDecimal(8))));
    }

    @Test
    public void createNegativeAmount() {
        exception.expect(NegativeAmountException.class);
        exception.expectMessage(containsString("Amount cannot be negative, value: -10"));
        Amount.of(new BigDecimal(-10));
    }


}