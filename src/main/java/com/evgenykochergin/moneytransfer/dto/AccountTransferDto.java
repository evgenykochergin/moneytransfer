package com.evgenykochergin.moneytransfer.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
public class AccountTransferDto {
    private final UUID to;
    private final BigDecimal amount;

    @JsonCreator
    public AccountTransferDto(@JsonProperty("to") UUID to,
                              @JsonProperty("amount") BigDecimal amount) {
        this.to = to;
        this.amount = amount;
    }
}
