package com.evgenykochergin.moneytransfer.model;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class Account {
    private final UUID id;
    private final Amount amount;
    private final Currency currency;
}
