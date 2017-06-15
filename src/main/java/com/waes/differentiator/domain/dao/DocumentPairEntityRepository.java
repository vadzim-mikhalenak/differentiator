package com.waes.differentiator.domain.dao;

import com.waes.differentiator.domain.model.DocumentPairEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Vadzim Mikhalenak.
 */
public interface DocumentPairEntityRepository extends JpaRepository<DocumentPairEntity, Long> {

}
