package com.waes.differentiator.client.model;

/**
 * @author Vadzim Mikhalenak.
 */
public class ErrorResponse implements JacksonSettings {

	private String errorMessage;

	public ErrorResponse(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
