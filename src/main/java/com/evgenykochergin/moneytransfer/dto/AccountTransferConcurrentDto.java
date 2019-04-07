package com.evgenykochergin.moneytransfer.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.UUID;

@Value
public class AccountTransferConcurrentDto {

    private final String message;
    private final UUID from;
    private final UUID to;

    @JsonCreator
    public AccountTransferConcurrentDto(@JsonProperty("message") String message,
                                        @JsonProperty("from") UUID from,
                                        @JsonProperty("to") UUID to) {
        this.message = message;
        this.from = from;
        this.to = to;
    }
}
