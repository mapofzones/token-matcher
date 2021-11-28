package com.mapofzones.tokenmatcher.common.exceptions;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends BaseException {

    public EntityNotFoundException(String message, String entityId) {
        super(message, ImmutableMap.of("entityId", entityId));
    }

    public EntityNotFoundException(String entityId) {
        super(ExceptionMessages.ERROR_ENTITY_NOT_FOUND, ImmutableMap.of("entityId", entityId));
    }

}
