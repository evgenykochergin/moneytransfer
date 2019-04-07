package com.evgenykochergin.moneytransfer.model;

import com.evgenykochergin.moneytransfer.model.exception.NegativeAmountException;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class Amount {
    private final BigDecimal value;

    private Amount(BigDecimal value) {
        this.value = value;
    }

    public static Amount of(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) >= 0) {
            return new Amount(value);
        }
        throw new NegativeAmountException("Amount cannot be negative, value: " + value);
    }

    public Amount add(Amount amount) {
        return Amount.of(this.value.add(amount.getValue()));
    }

    public Amount subtract(Amount amount) {
        return Amount.of((this.value.subtract(amount.getValue())));
    }
}
