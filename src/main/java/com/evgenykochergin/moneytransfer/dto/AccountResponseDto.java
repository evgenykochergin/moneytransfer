package com.evgenykochergin.moneytransfer.dto;

import com.evgenykochergin.moneytransfer.model.Account;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
public class AccountResponseDto {
    private final UUID id;
    private final BigDecimal amount;

    @JsonCreator
    public AccountResponseDto(@JsonProperty("id") UUID id,
                              @JsonProperty("amount") BigDecimal amount) {
        this.id = id;
        this.amount = amount;
    }

    public static AccountResponseDto of(Account account) {
        return new AccountResponseDto(account.getId(), account.getAmount().getValue());
    }
}
