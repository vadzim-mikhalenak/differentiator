package com.waes.differentiator.domain.dao;

import com.waes.differentiator.domain.model.DocumentPartEntity;
import com.waes.differentiator.domain.model.DocumentPartId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Vadzim Mikhalenak.
 */
public interface DocumentPartEntityRepository extends JpaRepository<DocumentPartEntity, DocumentPartId> {

}
