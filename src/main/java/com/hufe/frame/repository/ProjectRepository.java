package com.hufe.frame.repository;

import com.hufe.frame.model.ProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

  Optional<ProjectEntity> findByNameAndIdNot(String name, Long id);

  @Query("select count(u) from ProjectEntity u where u.name like %:keyword% and u.isActive = true")
  long count(@Param("keyword") String keyword);

  @Query("select count(u) from ProjectEntity u where u.isActive = true")
  long count();

  @Query("select u from ProjectEntity u where u.name like %:keyword% and u.isActive = true order by u.id DESC")
  Page<ProjectEntity> findAll(@Param("keyword") String keyword, Pageable pageable);

  @Query("select u from ProjectEntity u where u.isActive = true order by u.id DESC")
  Page<ProjectEntity> findAll(Pageable pageable);

}
