package com.waes.differentiator.domain.dao;

import com.waes.differentiator.domain.model.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Vadzim Mikhalenak.
 */
public interface DocumentEntityRepository extends JpaRepository<DocumentEntity, Long> {

	//	@Lock(LockModeType.PESSIMISTIC_WRITE)
	DocumentEntity findDocumentForUpdateById(Long id);
}
