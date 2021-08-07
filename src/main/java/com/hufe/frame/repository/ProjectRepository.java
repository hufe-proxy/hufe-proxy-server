package com.hufe.frame.repository;

import com.hufe.frame.model.ProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

  long countByNameContainingAndIsActiveTrue(@Param("keyword") String keyword);

  long countByIsActiveTrue();

  Page<ProjectEntity> findByNameContainingAndIsActiveTrueOrderByIdDesc(@Param("keyword") String keyword, Pageable pageable);

  Page<ProjectEntity> findByIsActiveTrueOrderByIdDesc(Pageable pageable);

  Optional<ProjectEntity> findByNameAndIdNot(String name, Long id);

  ProjectEntity findTop1ByNameContaining(String projectName);

}
