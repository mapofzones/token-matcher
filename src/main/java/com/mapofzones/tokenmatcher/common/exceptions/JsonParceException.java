package com.mapofzones.tokenmatcher.common.exceptions;

public class JsonParceException extends BaseException{

	protected JsonParceException(String message) {
		super(message);
	}

	public JsonParceException(String message, Throwable cause) {
		super(message, cause);
    }	
}
