package com.evgenykochergin.moneytransfer.service.impl;

import com.evgenykochergin.moneytransfer.model.Account;
import com.evgenykochergin.moneytransfer.model.Amount;
import com.evgenykochergin.moneytransfer.persistance.TransactionManager;
import com.evgenykochergin.moneytransfer.repository.AccountRepository;
import com.evgenykochergin.moneytransfer.service.AccountService;

import javax.inject.Inject;
import java.util.Collection;
import java.util.UUID;

public class AccountServiceImpl implements AccountService {

    private final TransactionManager transactionManager;

    private final AccountRepository accountRepository;

    @Inject
    public AccountServiceImpl(TransactionManager transactionManager, AccountRepository accountRepository) {
        this.transactionManager = transactionManager;
        this.accountRepository = accountRepository;
    }

    @Override
    public Account get(UUID id) {
        return transactionManager.doInTransaction(() -> accountRepository.find(id));
    }

    @Override
    public Collection<Account> getAll() {
        return transactionManager.doInTransaction(accountRepository::findAll);
    }

    @Override
    public Account add(Account account) {
        return transactionManager.doInTransaction(() -> accountRepository.save(account));

    }

    @Override
    public Account update(Account account) {
        return transactionManager.doInTransaction(() -> accountRepository.update(account));
    }

    @Override
    public void remove(UUID id) {
        transactionManager.doInTransaction(() -> {
            accountRepository.delete(id);
            return null;
        });
    }

    @Override
    public void transfer(UUID from, UUID to, Amount amount) {
        transactionManager.doInTransaction(() -> {
            Account accountFrom = accountRepository.find(from);
            Account accountTo = accountRepository.find(to);
            Amount amountFrom = accountFrom.getAmount();
            Amount amountTo = accountTo.getAmount();
            if (amountFrom.getValue().compareTo(amount.getValue()) >= 0) {
                Account updatedAccountFrom = accountFrom
                        .toBuilder()
                        .amount(Amount.of(amountFrom.getValue().subtract(amount.getValue())))
                        .build();
                Account updatedAccountTo = accountTo
                        .toBuilder()
                        .amount(Amount.of(amountTo.getValue().add(amount.getValue())))
                        .build();
                accountRepository.update(updatedAccountFrom);
                accountRepository.update(updatedAccountTo);
            }
            return null;
        });
    }

}
