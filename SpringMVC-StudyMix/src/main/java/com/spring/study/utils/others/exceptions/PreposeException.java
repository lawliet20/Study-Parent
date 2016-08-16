package com.spring.study.utils.others.exceptions;

public class PreposeException extends Exception {
	public PreposeException(int errorcode) {
		super();
		this.errorcode = errorcode;
	}
	
	public int getErrorCode() {
		return this.errorcode;
	}
	
	public static final int ERROR_PIN_DATA = 99;
	
//	public static final int ERROR_CRYPTOGRAPHIC_MESSAGE_OUT_RANGE 	= 10;
//	public static final int ERROR_CRYPTOGRAPHIC_CIPHER_OUT_RANGE 	= 11;
//	public static final int ERROR_CRYPTOGRAPHIC_SIGNATURE_OUT_RANGE = 12;

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1400355979065597090L;

	int errorcode;
}
