package com.evgenykochergin.moneytransfer.dto;

import com.evgenykochergin.moneytransfer.model.Account;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Value
public class AccountDto {
    private final UUID id;
    private final BigDecimal amount;

    public static AccountDto of(Account account) {
        return new AccountDto(account.getId(), account.getAmount().getValue());
    }
}
