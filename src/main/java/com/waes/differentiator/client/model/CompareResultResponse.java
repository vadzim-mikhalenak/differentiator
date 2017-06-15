package com.waes.differentiator.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * @author Vadzim Mikhalenak.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompareResultResponse implements JacksonSettings {

	private Status status;
	private List<DiffPartResponse> diffs;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<DiffPartResponse> getDiffs() {
		return diffs;
	}

	public void setDiffs(List<DiffPartResponse> diffs) {
		this.diffs = diffs;
	}

	public enum Status {
		EQUAL,
		DIFFERENT_SIZE,
		DIFFERENT_CONTENT
	}
}



