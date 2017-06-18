package com.waes.differentiator.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * Entity representation of Document with left and right parts.
 *
 * @author Vadzim Mikhalenak.
 */
@Entity
@Table(name = "DOCUMENT")
public class DocumentEntity {

	@Id
	private Long id;

	@Column
	private byte[] left;

	@Column
	private byte[] right;

	@Version
	@Column(name = "OPTLOCK")
	private int version;

	public DocumentEntity(Long id) {
		this.id = id;
	}

	protected DocumentEntity() {

	}

	public Long getId() {
		return id;
	}

	public byte[] getLeft() {
		return left;
	}

	public void setLeft(byte[] left) {
		this.left = left;
	}

	public byte[] getRight() {
		return right;
	}

	public void setRight(byte[] right) {
		this.right = right;
	}

	public int getVersion() {
		return version;
	}
}
