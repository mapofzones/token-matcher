package com.mapofzones.tokenmatcher.common.exceptions;

public class JsonParseException extends BaseException{

	protected JsonParseException(String message) {
		super(message);
	}

	public JsonParseException(String message, Throwable cause) {
		super(message, cause);
    }	
}
