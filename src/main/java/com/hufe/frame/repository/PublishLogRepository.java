package com.hufe.frame.repository;

import com.hufe.frame.dataobject.bo.PublishLogShowBO;
import com.hufe.frame.model.PublishLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PublishLogRepository extends JpaRepository<PublishLogEntity, Long> {

  @Query("select count(pl.id) from PublishLogEntity pl " +
    "LEFT JOIN ProjectEntity p ON pl.projectId = p.id " +
    "LEFT JOIN UserEntity u ON pl.userId = u.id " +
    "WHERE pl.projectId = :projectId and pl.isActive = true")
  long count(@Param("projectId") Long projectId);

  @Query("select count(pl.id) from PublishLogEntity pl " +
    "LEFT JOIN ProjectEntity p ON pl.projectId = p.id " +
    "LEFT JOIN UserEntity u ON pl.userId = u.id " +
    "WHERE pl.isActive = true")
  long count();

  @Query("select new com.hufe.frame.dataobject.bo.PublishLogShowBO(pl.id,pl.name,pl.uuid,p.name,p.sourceType,u.name,pl.createAt) from PublishLogEntity pl " +
    "LEFT JOIN ProjectEntity p ON pl.projectId = p.id " +
    "LEFT JOIN UserEntity u ON pl.userId = u.id " +
    "WHERE pl.projectId = :projectId and pl.isActive = true " +
    "ORDER BY pl.createAt DESC")
  Page<PublishLogShowBO> findPublishLogAll(@Param("projectId") Long projectId, Pageable pageable);

  @Query("select new com.hufe.frame.dataobject.bo.PublishLogShowBO(pl.id,pl.name,pl.uuid,p.name,p.sourceType,u.name,pl.createAt) from PublishLogEntity pl " +
    "LEFT JOIN ProjectEntity p ON pl.projectId = p.id " +
    "LEFT JOIN UserEntity u ON pl.userId = u.id " +
    "WHERE pl.isActive = true " +
    "ORDER BY pl.createAt DESC")
  Page<PublishLogShowBO> findPublishLogAll(Pageable pageable);

}
