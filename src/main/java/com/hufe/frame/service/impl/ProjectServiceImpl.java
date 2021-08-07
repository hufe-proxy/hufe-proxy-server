package com.hufe.frame.service.impl;

import com.hufe.frame.dataobject.ao.project.CreateProjectAO;
import com.hufe.frame.dataobject.ao.project.DeleteProjectAO;
import com.hufe.frame.dataobject.ao.project.SearchProjectAO;
import com.hufe.frame.dataobject.ao.project.UpdateProjectAO;
import com.hufe.frame.dataobject.po.exception.FrameMessageException;
import com.hufe.frame.dataobject.vo.project.ProjectOptionVO;
import com.hufe.frame.dataobject.vo.project.ProjectShowVO;
import com.hufe.frame.dataobject.vo.project.ProjectVO;
import com.hufe.frame.model.ProjectEntity;
import com.hufe.frame.repository.ProjectRepository;
import com.hufe.frame.service.ProjectService;
import com.hufe.frame.util.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

  @Value("${minio.bucketName}")
  private String bucketName;

  @Autowired
  private MinioUtil minioUtil;

  @Autowired
  private ProjectRepository projectRepository;

  @Override
  public ProjectShowVO getList(SearchProjectAO params) {
    long count;
    Page<ProjectEntity> raw = null;
    String keyword = params.getKeyword();
    Pageable pageable = PageRequest.of(params.getPageNo(), params.getPageSize());
    if (!"".equals(keyword) && keyword != null) {
      count = projectRepository.countByNameContainingAndIsActiveTrue(keyword);
      raw = projectRepository.findByNameContainingAndIsActiveTrueOrderByIdDesc(keyword, pageable);
    } else {
      count = projectRepository.countByIsActiveTrue();
      raw = projectRepository.findByIsActiveTrueOrderByIdDesc(pageable);
    }
    return ProjectShowVO.builder()
            .count(count)
            .data(raw.stream().map(r -> ProjectVO.builder()
                    .id(r.getId())
                    .name(r.getName())
                    .sourceType(r.getSourceType())
                    .createAt(r.getCreateAt())
                    .build()).collect(Collectors.toList()))
            .build();
  }

  @Override
  public List<ProjectOptionVO> getAll() {
    List<ProjectEntity> projectList = projectRepository.findAll();
    return projectList.stream().map(p -> ProjectOptionVO.builder()
            .value(p.getId())
            .label(p.getName())
            .build()).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void addProject(CreateProjectAO params) {
    // 验证项目是否已经存在
    Example<ProjectEntity> example = Example.of(
            ProjectEntity.builder()
                    .name(params.getName())
                    .build()
    );
    Optional<ProjectEntity> projectOptional = projectRepository.findOne(example);
    if (projectOptional.isPresent()) {
      throw new FrameMessageException("项目已经存在");
    }
    try {
      String name = params.getName();
      // 增加项目
      projectRepository.save(ProjectEntity.builder()
              .name(name)
              .sourceType(params.getSourceType().getValue())
              .build());
      // 创建minio项目文件夹
      InputStream inputStream = this.getClass().getResourceAsStream("/static/canary.README");
      minioUtil.putObject(bucketName, name + "/" + name + ".README", inputStream);
    } catch (Exception error) {
      throw new FrameMessageException(error.toString());
    }
  }

  @Override
  @Transactional
  public void deleteProject(DeleteProjectAO params) {
    // 验证项目是否存在
    Optional<ProjectEntity> projectOptional = projectRepository.findById(params.getId());
    if (!projectOptional.isPresent()) {
      throw new FrameMessageException("项目不存在");
    }
    // 删除项目
    ProjectEntity project = projectOptional.get();
    project.setIsActive(false);
    projectRepository.save(project);
  }

  @Override
  @Transactional
  public void updateProject(UpdateProjectAO params) {
    // 验证项目是否存在
    Optional<ProjectEntity> projectOptional = projectRepository.findById(params.getId());
    if (!projectOptional.isPresent()) {
      throw new FrameMessageException("项目不存在");
    }
    ProjectEntity project = projectOptional.get();
    String name = project.getName();
    // 验证项目名称是否已经存在
    Optional<ProjectEntity> existProject = projectRepository.findByNameAndIdNot(params.getName(), params.getId());
    if (existProject.isPresent()) {
      throw new FrameMessageException("项目名称已经存在");
    }
    // 更新项目记录
    project.setName(params.getName());
    project.setSourceType(params.getSourceType().getValue());
    projectRepository.save(project);
    // 更新minio
    if (!name.equals(params.getName())) {
      try {
        // 移动到新目录
        minioUtil.moveObject(bucketName, params.getName(), name);
      } catch (Exception error) {
        throw new FrameMessageException(error.toString());
      }
    }
  }

  @Override
  @Transactional
  public ProjectEntity findTop1ByNameContaining(String projectName) {
    return projectRepository.findTop1ByNameContaining(projectName);
  }

}
