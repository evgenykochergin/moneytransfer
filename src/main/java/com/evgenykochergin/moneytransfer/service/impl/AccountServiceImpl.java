package com.evgenykochergin.moneytransfer.service.impl;

import com.evgenykochergin.moneytransfer.model.Account;
import com.evgenykochergin.moneytransfer.model.Amount;
import com.evgenykochergin.moneytransfer.model.exception.NegativeAmountException;
import com.evgenykochergin.moneytransfer.persistance.TransactionManagement;
import com.evgenykochergin.moneytransfer.repository.AccountRepository;
import com.evgenykochergin.moneytransfer.service.AccountService;
import com.evgenykochergin.moneytransfer.model.exception.AccountWithdrawNotEnoughAmountException;

import javax.inject.Inject;
import java.util.Collection;
import java.util.UUID;

public class AccountServiceImpl implements AccountService {

    private final TransactionManagement transactionManagement;

    private final AccountRepository accountRepository;

    @Inject
    public AccountServiceImpl(TransactionManagement transactionManagement, AccountRepository accountRepository) {
        this.transactionManagement = transactionManagement;
        this.accountRepository = accountRepository;
    }

    @Override
    public Account get(UUID id) {
        return transactionManagement.doInTransaction(() -> accountRepository.find(id));
    }

    @Override
    public Collection<Account> getAll() {
        return transactionManagement.doInTransaction(accountRepository::findAll);
    }

    @Override
    public Account add(Account account) {
        return transactionManagement.doInTransaction(() -> accountRepository.save(account));

    }

    @Override
    public Account update(Account account) {
        return transactionManagement.doInTransaction(() -> accountRepository.update(account));
    }

    @Override
    public void remove(UUID id) {
        transactionManagement.doInTransaction(() -> {
            accountRepository.delete(id);
            return null;
        });
    }

    @Override
    public void transfer(UUID from, UUID to, Amount amount) {
        transactionManagement.doInTransaction(() -> {
            tryTransfer(accountRepository.find(from), accountRepository.find(to), amount);
            return null;
        });
    }

    @Override
    public void transfer(Account from, Account to, Amount amount) {
        transactionManagement.doInTransaction(() -> {
            tryTransfer(from, to, amount);
            return null;
        });
    }

    private void tryTransfer(Account from, Account to, Amount amount) {
        try {
            Account accountFromToUpdate = from.withdraw(amount);
            Account accountToToUpdate = to.deposit(amount);
            accountRepository.update(accountFromToUpdate);
            accountRepository.update(accountToToUpdate);
        } catch (NegativeAmountException e) {
            throw new AccountWithdrawNotEnoughAmountException(from.getId(), e);
        }
    }

}
