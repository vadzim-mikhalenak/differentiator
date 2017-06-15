package com.waes.differentiator.domain.model;

import com.waes.differentiator.client.model.JacksonSettings;

import java.util.List;

/**
 * Represents result of comparison of two parts of document
 *
 * @author Vadzim Mikhalenak.
 */
public class CompareResult implements JacksonSettings {

	private Long id;
	private Status status;
	private List<DiffPart> diffs;

	public CompareResult(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<DiffPart> getDiffs() {
		return diffs;
	}

	public void setDiffs(List<DiffPart> diffs) {
		this.diffs = diffs;
	}

	public static class DiffPart {

		private Integer offset;
		private Integer size;

		public DiffPart(Integer offset, Integer size) {
			this.offset = offset;
			this.size = size;
		}

		public Integer getOffset() {
			return offset;
		}

		public Integer getSize() {
			return size;
		}
	}

	public enum Status {
		EQUAL,
		DIFFERENT_SIZE,
		DIFFERENT_CONTENT
	}
}
