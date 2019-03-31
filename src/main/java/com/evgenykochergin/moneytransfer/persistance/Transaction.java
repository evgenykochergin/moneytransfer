package com.evgenykochergin.moneytransfer.persistance;

public interface Transaction<T> {

    T execute();
}
