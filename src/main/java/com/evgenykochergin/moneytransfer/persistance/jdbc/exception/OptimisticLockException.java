package com.evgenykochergin.moneytransfer.persistance.jdbc.exception;

import java.util.UUID;

public class OptimisticLockException extends RuntimeException {

    public OptimisticLockException(UUID id, Class clazz) {
        super("Optimistic lock for entity `" + clazz.getName() + "` with id " + id);
    }
}
