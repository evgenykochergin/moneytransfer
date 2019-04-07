package com.evgenykochergin.moneytransfer.guice.module;

import com.evgenykochergin.moneytransfer.model.Account;
import com.evgenykochergin.moneytransfer.model.Amount;
import com.evgenykochergin.moneytransfer.service.AccountService;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

public class TestDataAccountModule extends AbstractModule {

    @Override
    protected void configure() {
        requestInjection(this);
    }

    @Inject
    private void addTestData(AccountService accountService) {
        IntStream.range(1, 5).forEach((a) -> accountService.add(createRandomAccount()));
    }

    private Account createRandomAccount() {
        return Account.builder()
                .id(UUID.randomUUID())
                .version(0)
                .amount(Amount.of(new BigDecimal(new Random().nextInt(1000000))))
                .build();
    }
}
