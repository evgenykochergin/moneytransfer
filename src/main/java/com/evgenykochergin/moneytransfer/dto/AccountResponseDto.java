package com.evgenykochergin.moneytransfer.dto;

import com.evgenykochergin.moneytransfer.model.Account;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
public class AccountResponseDto {
    private final UUID id;
    private final BigDecimal amount;

    public static AccountResponseDto of(Account account) {
        return new AccountResponseDto(account.getId(), account.getAmount().getValue());
    }
}
