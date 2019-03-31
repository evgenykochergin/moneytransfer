package com.evgenykochergin.moneytransfer.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class AccountRequestDto {
    private final BigDecimal amount;

    @JsonCreator
    public AccountRequestDto(@JsonProperty("amount") BigDecimal amount) {
        this.amount = amount;
    }
}

