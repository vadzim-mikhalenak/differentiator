package com.waes.differentiator.domain.service;

import com.waes.differentiator.domain.dao.DocumentPairEntityRepository;
import com.waes.differentiator.domain.model.CompareResult;
import com.waes.differentiator.domain.model.CompareResult.DiffPart;
import com.waes.differentiator.domain.model.DocumentPairEntity;
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

	private final DocumentPairEntityRepository documentPairDao;

	@Autowired
	public DocumentService(DocumentPairEntityRepository documentPairDao) {
		this.documentPairDao = documentPairDao;
	}

	/**
	 * Updates 'left' part of the document
	 *
	 * @param id   document id
	 * @param left content as byte array
	 */
	@Transactional
	public void updateDocumentWithLeft(Long id, byte[] left) {
		DocumentPairEntity documentPair = documentPairDao.findOne(id);
		if (documentPair == null) {
			documentPair = new DocumentPairEntity(id);
		}
		documentPair.setLeft(left);
		documentPairDao.save(documentPair);
	}

	/**
	 * Updates 'right' part of the document
	 *
	 * @param id    document id
	 * @param right content as byte array
	 */
	@Transactional
	public void updateDocumentWithRight(Long id, byte[] right) {
		DocumentPairEntity documentPair = documentPairDao.findOne(id);
		if (documentPair == null) {
			documentPair = new DocumentPairEntity(id);
		}
		documentPair.setRight(right);
		documentPairDao.save(documentPair);
	}

	/**
	 * Compares to parts of the document.
	 *
	 * @param id document id
	 * @return Result object that contains status of comparison (EQUAL,	DIFFERENT_SIZE,	DIFFERENT_CONTENT)
	 * and list of diffs (offset+size) if two parts have the same size but different content
	 */
	public CompareResult diffDocument(Long id) {
		DocumentPairEntity documentPair = documentPairDao.findOne(id);
		if (documentPair == null) {
			return null;
		}
		byte[] left = documentPair.getLeft();
		byte[] right = documentPair.getRight();
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
				int size = 1;
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
	public DocumentPairEntity readDocument(Long id) {
		if (id == null) {
			return null;
		}
		return documentPairDao.findOne(id);
	}

	/**
	 * Deletes all documents
	 *
	 */
	public void deleteAllDocuments() {
		documentPairDao.deleteAll();
	}

}
