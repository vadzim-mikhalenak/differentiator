package com.waes.differentiator.domain.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;

/**
 * @author Vadzim Mikhalenak.
 */
@Embeddable
public class DocumentPartId implements Serializable {

	@OneToOne()
	@JoinColumn(name = "document_id")
	private DocumentEntity document;

	@Column(name = "part_type")
	@Enumerated(EnumType.STRING)
	private PartType partType;

	public DocumentPartId() {
	}

	public DocumentPartId(DocumentEntity document, PartType partType) {
		this.document = document;
		this.partType = partType;
	}

	public DocumentEntity getDocument() {
		return document;
	}

	public void setDocument(DocumentEntity document) {
		this.document = document;
	}

	public PartType getPartType() {
		return partType;
	}

	public void setPartType(PartType partType) {
		this.partType = partType;
	}
}
