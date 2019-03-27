package com.evgenykochergin.moneytransfer.model;

import lombok.Value;

import java.math.BigDecimal;

@Value(staticConstructor="of")
public class Amount {
    private final BigDecimal value;
}
