package com.evgenykochergin.moneytransfer.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Currency {
    private final UUID id;
    private final String name;
}
