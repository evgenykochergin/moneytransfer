package com.evgenykochergin.moneytransfer.repository.exception;

import java.util.UUID;

public class EntityNotFountException extends RuntimeException {


    public EntityNotFountException(UUID id, Class clazz) {
        super("Entity `" + clazz.getSimpleName() + "` not found with id " + id);
    }
}
