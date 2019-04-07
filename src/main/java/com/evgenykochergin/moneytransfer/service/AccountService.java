package com.evgenykochergin.moneytransfer.service;

import com.evgenykochergin.moneytransfer.model.Account;
import com.evgenykochergin.moneytransfer.model.Amount;

import java.util.Collection;
import java.util.UUID;

public interface AccountService {

    Account get(UUID id);

    Collection<Account> getAll();

    Account add(Account account);

    Account update(Account account);

    void remove(UUID id);

    void transfer(UUID from, UUID to, Amount amount);

    void transfer(Account from, Account to, Amount amount);

}
