package com.waes.differentiator.domain.model;

import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.List;

/**
 * Entity representation of Document with left and right parts.
 *
 * @author Vadzim Mikhalenak.
 */
@Entity
@Table(name = "DOCUMENT_PAIR")
public class DocumentEntity {

	@Id
	private Long id;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "id.document", fetch = FetchType.EAGER)
	@Where(clause = "part_type='LEFT'")
	private List<DocumentPartEntity> left;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "id.document", fetch = FetchType.EAGER)
	@Where(clause = "part_type='RIGHT'")
	private List<DocumentPartEntity> right;

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

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getLeft() {
		return left != null && !left.isEmpty() ? left.get(0).getContent() : null;
	}

	public byte[] getRight() {
		return right != null && !right.isEmpty() ? right.get(0).getContent() : null;
	}

	public int getVersion() {
		return version;
	}
}
