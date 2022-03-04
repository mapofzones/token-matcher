package com.mapofzones.tokenmatcher.common.exceptions;

public class UnsupportedOperationException extends BaseException{

	protected UnsupportedOperationException(String message) {
		super(message);
	}

	public UnsupportedOperationException(String message, Throwable cause) {
		super(message, cause);
    }	
}
