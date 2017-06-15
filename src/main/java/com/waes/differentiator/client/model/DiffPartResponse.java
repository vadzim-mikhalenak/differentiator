package com.waes.differentiator.client.model;

/**
 * @author Vadzim Mikhalenak.
 */
public class DiffPartResponse implements JacksonSettings {

	private Integer offset;
	private Integer size;

	public DiffPartResponse(Integer offset, Integer size) {
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
