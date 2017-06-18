package com.waes.differentiator.domain.service;

import com.waes.differentiator.domain.dao.DocumentEntityRepository;
import com.waes.differentiator.domain.dao.DocumentPartEntityRepository;
import com.waes.differentiator.domain.model.DocumentEntity;
import com.waes.differentiator.domain.model.DocumentPartId;
import com.waes.differentiator.domain.model.PartType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author Vadzim Mikhalenak.
 */
@Service
public class DocumentPartService {

	private final DocumentPartEntityRepository documentPartDao;
	private final DocumentEntityRepository documentDao;

	@Autowired
	public DocumentPartService(DocumentPartEntityRepository documentPartDao, DocumentEntityRepository documentDao) {
		this.documentPartDao = documentPartDao;
		this.documentDao = documentDao;
	}

	@Transactional
	public void setDocumentPartContent(Long documentId, PartType partType, byte[] content) {
		DocumentEntity documentEntity = documentDao.findOne(documentId);
		documentPartDao.findOne(new DocumentPartId(documentEntity, partType));
	}
}
