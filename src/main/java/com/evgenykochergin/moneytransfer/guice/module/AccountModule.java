package com.evgenykochergin.moneytransfer.guice.module;

import com.evgenykochergin.moneytransfer.model.Account;
import com.evgenykochergin.moneytransfer.model.Amount;
import com.evgenykochergin.moneytransfer.repository.AccountRepository;
import com.evgenykochergin.moneytransfer.repository.impl.JdbcAccountRepository;
import com.evgenykochergin.moneytransfer.rest.AccountRestEndpoint;
import com.evgenykochergin.moneytransfer.service.AccountService;
import com.evgenykochergin.moneytransfer.service.impl.AccountServiceImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

public class AccountModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AccountRepository.class).to(JdbcAccountRepository.class).in(Singleton.class);
        bind(AccountService.class).to(AccountServiceImpl.class).in(Singleton.class);
        bind(AccountRestEndpoint.class).in(Singleton.class);
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
