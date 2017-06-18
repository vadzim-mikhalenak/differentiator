package com.waes.differentiator.domain.service;

import com.waes.differentiator.domain.dao.DocumentEntityRepository;
import com.waes.differentiator.domain.dao.DocumentPartEntityRepository;
import com.waes.differentiator.domain.model.CompareResult;
import com.waes.differentiator.domain.model.CompareResult.DiffPart;
import com.waes.differentiator.domain.model.DocumentEntity;
import com.waes.differentiator.domain.model.DocumentPartEntity;
import com.waes.differentiator.domain.model.DocumentPartId;
import com.waes.differentiator.domain.model.PartType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * Service is designed to work with documents (create, update, etc.)
 *
 * @author Vadzim Mikhalenak.
 */
@Service
public class DocumentService {

	private final DocumentEntityRepository documentDao;
	private final DocumentPartEntityRepository documentPartDao;

	@Autowired
	public DocumentService(DocumentEntityRepository documentDao, DocumentPartEntityRepository documentPartDao) {
		this.documentDao = documentDao;
		this.documentPartDao = documentPartDao;
	}

	/**
	 * Updates 'left' part of the document
	 *
	 * @param id   document id
	 * @param left content as byte array
	 */
	@Transactional
	public void updateDocumentWithLeft(Long id, byte[] left) {
		DocumentEntity document = documentDao.findDocumentForUpdateById(id);

		if (document == null) {
			document = documentDao.save(new DocumentEntity(id));
		}

		DocumentPartId documentPartId = new DocumentPartId(document, PartType.LEFT);
		DocumentPartEntity documentPart = documentPartDao.findOne(documentPartId);
		if (documentPart == null) {
			documentPart = new DocumentPartEntity();
			documentPart.setId(documentPartId);
		}
		documentPart.setContent(left);
		documentPartDao.save(documentPart);
		//document.setLeft(left);

		//documentDao.save(document);
	}

	/**
	 * Updates 'right' part of the document
	 *
	 * @param id    document id
	 * @param right content as byte array
	 */
	@Transactional
	public void updateDocumentWithRight(Long id, byte[] right) {
		DocumentEntity document = documentDao.findDocumentForUpdateById(id);

		if (document == null) {
			document = documentDao.save(new DocumentEntity(id));
		}

		DocumentPartId documentPartId = new DocumentPartId(document, PartType.RIGHT);
		DocumentPartEntity documentPart = documentPartDao.findOne(documentPartId);
		if (documentPart == null) {
			documentPart = new DocumentPartEntity();
			documentPart.setId(documentPartId);
		}
		documentPart.setContent(right);
		documentPartDao.save(documentPart);
	}

	/**
	 * Compares to parts of the document.
	 *
	 * @param id document id
	 * @return Result object that contains status of comparison (EQUAL,	DIFFERENT_SIZE,	DIFFERENT_CONTENT)
	 * and list of diffs (offset+size) if two parts have the same size but different content
	 */
	public CompareResult diffDocument(Long id) {
		DocumentEntity document = documentDao.findOne(id);
		if (document == null) {
			return null;
		}
		byte[] left = document.getLeft();
		byte[] right = document.getRight();
		CompareResult compareResult = new CompareResult(id);

		if (left == null && right == null) {
			compareResult.setStatus(CompareResult.Status.EQUAL);
			return compareResult;
		}
		if (left == null || right == null || left.length != right.length) {
			compareResult.setStatus(CompareResult.Status.DIFFERENT_SIZE);
			return compareResult;
		}

		List<DiffPart> diffs = new LinkedList<>();

		for (int i = 0; i < left.length; i++) {
			if (left[i] != right[i]) {
				int offset = i;
				int size = 0;
				while (i < left.length && left[i] != right[i]) {
					size++;
					i++;
				}
				diffs.add(new DiffPart(offset, size));
			}
		}

		if (diffs.isEmpty()) {
			compareResult.setStatus(CompareResult.Status.EQUAL);
		} else {
			compareResult.setStatus(CompareResult.Status.DIFFERENT_CONTENT);
			compareResult.setDiffs(diffs);
		}

		return compareResult;
	}

	/**
	 * Returns document by the id provided
	 *
	 * @param id document id
	 * @return Document entity
	 */
	public DocumentEntity readDocument(Long id) {
		if (id == null) {
			return null;
		}
		return documentDao.findOne(id);
	}

	/**
	 * Deletes all documents
	 */
	public void deleteAllDocuments() {
		documentDao.deleteAll();
	}

}
