package com.hufe.frame.repository;

import com.hufe.frame.model.AccessLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessLogRepository extends JpaRepository<AccessLogEntity, Long> {
}
