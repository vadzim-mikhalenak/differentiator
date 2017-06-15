package com.waes.differentiator.client.model;

/**
 * Client model for interaction between Client and the Server.
 *
 * @author Vadzim Mikhalenak.
 */
public class DocumentPartRequest implements JacksonSettings {

	private String content;

	public DocumentPartRequest() {
	}

	public DocumentPartRequest(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
