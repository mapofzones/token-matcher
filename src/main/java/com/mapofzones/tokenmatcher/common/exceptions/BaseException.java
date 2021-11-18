package com.mapofzones.tokenmatcher.common.exceptions;

import java.util.Collections;
import java.util.Map;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
	
	private Map<String, String> params;
	
	protected BaseException(String message) {
        super(message);
        this.params = Collections.emptyMap();
    }
	
	protected BaseException(String message, Map<String, String> params) {
        super(message);
        this.params = params != null ? params : Collections.emptyMap();
    }

    protected BaseException(String message, Map<String, String> params, Throwable cause) {
        super(message, cause);
        this.params = params != null ? params : Collections.emptyMap();
    }
    
    protected BaseException(String message, Throwable cause) {
		super(message, cause);
    }
}
