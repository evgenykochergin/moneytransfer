package com.evgenykochergin.moneytransfer.persistance;

public interface TransactionCallback<T> {

    T call();
}
