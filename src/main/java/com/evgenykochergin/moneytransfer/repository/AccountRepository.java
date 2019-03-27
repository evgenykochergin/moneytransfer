package com.evgenykochergin.moneytransfer.repository;

import com.evgenykochergin.moneytransfer.model.Account;

import java.util.UUID;

public interface AccountRepository extends CrudRepository<Account, UUID> {
}
