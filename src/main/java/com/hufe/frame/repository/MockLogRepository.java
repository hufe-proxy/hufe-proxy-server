package com.hufe.frame.repository;

import com.hufe.frame.model.MockLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MockLogRepository extends JpaRepository<MockLogEntity, Long> {

  @Query("select count(u) from MockLogEntity u where u.name like '%:keyword%' and u.isActive = true")
  long count(@Param("keyword") String keyword);

  @Query("select count(u) from MockLogEntity u where u.isActive = true")
  long count();

  @Query("select u from MockLogEntity u where u.name like '%:keyword%' and u.isActive = true order by u.id DESC")
  Page<MockLogEntity> findAll(@Param("keyword") String keyword, Pageable pageable);

  @Query("select u from MockLogEntity u where u.isActive = true order by u.id DESC")
  Page<MockLogEntity> findAll(Pageable pageable);

}
