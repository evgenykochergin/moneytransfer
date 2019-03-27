package com.evgenykochergin.moneytransfer.service;

import com.evgenykochergin.moneytransfer.model.Account;
import com.evgenykochergin.moneytransfer.model.Amount;

import java.util.UUID;

public interface AccountService {

    Account get(UUID id);

    void add(Account account);

    void remove(UUID id);

    void transfer(UUID from, UUID to, Amount amount);

}
