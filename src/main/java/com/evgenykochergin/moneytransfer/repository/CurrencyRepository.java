package com.evgenykochergin.moneytransfer.repository;

import com.evgenykochergin.moneytransfer.model.Currency;

import java.util.UUID;

public interface CurrencyRepository extends CrudRepository<Currency, UUID> {
}
