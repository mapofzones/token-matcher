package com.mapofzones.tokenmatcher.common.exceptions;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends BaseException {
	
	public EntityNotFoundException(String message, Map<String, String> params) {
        super(message, params);
    }

    public EntityNotFoundException(String message, String entityId) {
        super(message, ImmutableMap.of("entityId", entityId));
    }

    public EntityNotFoundException(Map<String, String> params) {
        super(ExceptionMessages.ERROR_ENTITY_NOT_FOUND, params);
    }

    public EntityNotFoundException(String entityId) {
        super(ExceptionMessages.ERROR_ENTITY_NOT_FOUND, ImmutableMap.of("entityId", entityId));
    }

}
