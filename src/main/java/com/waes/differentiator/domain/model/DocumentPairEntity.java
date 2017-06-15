package com.waes.differentiator.domain.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity representation of Document with left and right parts.
 *
 * @author Vadzim Mikhalenak.
 */
@Entity
@Table(name = "DOCUMENT_PAIR")
public class DocumentPairEntity {

	@Id
	private Long id;
	private byte[] left;
	private byte[] right;

	public DocumentPairEntity(Long id) {
		this.id = id;
	}

	protected DocumentPairEntity() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
}
