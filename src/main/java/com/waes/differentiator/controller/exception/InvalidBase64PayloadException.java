package com.waes.differentiator.controller.exception;

/**
 * @author Vadzim Mikhalenak.
 */
public class InvalidBase64PayloadException extends RuntimeException {

	private final String sourceBase64Payload;

	public InvalidBase64PayloadException(String sourceBase64Payload) {
		this.sourceBase64Payload = sourceBase64Payload;
	}

	public String getSourceBase64Payload() {
		return sourceBase64Payload;
	}
}
