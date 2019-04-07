package com.evgenykochergin.moneytransfer.persistance.jdbc.exception;

import java.util.UUID;

public class JdbcOptimisticLockException extends RuntimeException {

    public JdbcOptimisticLockException(UUID id, Class clazz) {
        super("Optimistic lock for entity `" + clazz.getName() + "` with id " + id);
    }
}
