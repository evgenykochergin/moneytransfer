package com.evgenykochergin.moneytransfer.repository.exception;

import java.util.UUID;

public class OptimisticLockException extends RuntimeException {

    public OptimisticLockException(UUID id, Class clazz) {
        super("Optimistic lock for entity `" + clazz.getName() + "` with id " + id);
    }
}
