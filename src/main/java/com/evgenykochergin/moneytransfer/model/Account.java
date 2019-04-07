package com.evgenykochergin.moneytransfer.model;

import com.evgenykochergin.moneytransfer.model.exception.AccountDepositZeroAmountException;
import com.evgenykochergin.moneytransfer.model.exception.AccountWithdrawNotEnoughAmountException;
import com.evgenykochergin.moneytransfer.model.exception.NegativeAmountException;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
public class Account {
    private final UUID id;
    private final Amount amount;
    private final int version;

    public Account withdraw(Amount amount) {
        try {
            return this.toBuilder()
                    .amount(this.getAmount().subtract(amount))
                    .build();
        } catch (NegativeAmountException e) {
            throw new AccountWithdrawNotEnoughAmountException(this.getId(), e);
        }

    }

    public Account deposit(Amount amount) {
        if (amount.getValue().compareTo(BigDecimal.ZERO) == 0) {
            throw new AccountDepositZeroAmountException(this.getId());
        }
        return this.toBuilder()
                .amount(this.getAmount().add(amount))
                .build();
    }

}
