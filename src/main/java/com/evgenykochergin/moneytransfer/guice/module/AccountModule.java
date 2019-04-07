package com.evgenykochergin.moneytransfer.guice.module;

import com.evgenykochergin.moneytransfer.repository.AccountRepository;
import com.evgenykochergin.moneytransfer.repository.impl.JdbcAccountRepository;
import com.evgenykochergin.moneytransfer.rest.AccountRestEndpoint;
import com.evgenykochergin.moneytransfer.service.AccountService;
import com.evgenykochergin.moneytransfer.service.impl.AccountServiceImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class AccountModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AccountRepository.class).to(JdbcAccountRepository.class).in(Singleton.class);
        bind(AccountService.class).to(AccountServiceImpl.class).in(Singleton.class);
        bind(AccountRestEndpoint.class).in(Singleton.class);
    }
}
