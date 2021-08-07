package com.hufe.frame.repository;

import com.hufe.frame.model.MockLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface MockLogRepository extends JpaRepository<MockLogEntity, Long> {

  long countByIsActiveTrue();

  long countByAddressContainingAndIsActiveTrue(@Param("keyword") String keyword);

  Page<MockLogEntity> findByAddressContainingAndIsActiveTrueOrderByIdDesc(@Param("keyword") String keyword, Pageable pageable);

  Page<MockLogEntity> findByIsActiveTrueOrderByIdDesc(Pageable pageable);

}
