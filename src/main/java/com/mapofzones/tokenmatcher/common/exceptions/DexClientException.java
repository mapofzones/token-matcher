package com.mapofzones.tokenmatcher.common.exceptions;

public class DexClientException extends BaseException{

	protected DexClientException(String message) {
		super(message);
	}

	public DexClientException(String message, Throwable cause) {
		super(message, cause);
    }	
}
