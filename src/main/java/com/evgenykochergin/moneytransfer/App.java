package com.evgenykochergin.moneytransfer;

import com.evgenykochergin.moneytransfer.model.Account;
import com.evgenykochergin.moneytransfer.model.Amount;
import com.evgenykochergin.moneytransfer.model.Currency;

import java.math.BigDecimal;
import java.util.UUID;

public class App {
    public static void main(String[] args) {
        Currency currency = Currency
                .builder()
                .id(UUID.randomUUID())
                .name("USD")
                .build();
        Account account = Account
                .builder()
                .amount(Amount.of(new BigDecimal(100)))
                .currency(currency)
                .build();
        System.out.println("account" + account);
    }
}
