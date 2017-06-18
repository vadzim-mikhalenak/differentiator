package com.waes.differentiator.domain.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Vadzim Mikhalenak.
 */
@Entity
@Table(name = "DOCUMENT_PART")
public class DocumentPartEntity {

	@EmbeddedId
	private DocumentPartId id;

	private byte[] content;

	public DocumentPartId getId() {
		return id;
	}

	public void setId(DocumentPartId id) {
		this.id = id;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

/*	public DocumentEntity getDocument() {
		return document;
	}

	public void setDocument(DocumentEntity document) {
		this.document = document;
	}*/
}
