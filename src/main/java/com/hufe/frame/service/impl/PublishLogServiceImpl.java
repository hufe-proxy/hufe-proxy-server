
package com.hufe.frame.service.impl;

import com.hufe.frame.dataobject.ao.publishLog.CreatePublishLogAO;
import com.hufe.frame.dataobject.ao.publishLog.DeletePublishLogAO;
import com.hufe.frame.dataobject.ao.publishLog.SearchPublishLogAO;
import com.hufe.frame.dataobject.bo.PublishLogShowBO;
import com.hufe.frame.dataobject.po.exception.FrameMessageException;
import com.hufe.frame.dataobject.vo.publishLog.PublishLogShowVO;
import com.hufe.frame.dataobject.vo.publishLog.PublishLogVO;
import com.hufe.frame.model.ProjectEntity;
import com.hufe.frame.model.PublishLogEntity;
import com.hufe.frame.repository.ProjectRepository;
import com.hufe.frame.repository.PublishLogRepository;
import com.hufe.frame.service.PublishLogService;
import com.hufe.frame.util.CommonUtil;
import com.hufe.frame.util.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PublishLogServiceImpl implements PublishLogService {

  @Value("${minio.endpoint}")
  private String endpoint;

  @Value("${minio.bucketName}")
  private String bucketName;

  @Autowired
  private MinioUtil minioUtil;

  @Autowired
  private PublishLogRepository publishLogRepository;

  @Autowired
  private ProjectRepository projectRepository;

  @Override
  public PublishLogShowVO getList(SearchPublishLogAO params) {
    long count;
    Page<PublishLogShowBO> raw = null;
    Long projectId = params.getProjectId();
    Pageable pageable = PageRequest.of(params.getPageNo(), params.getPageSize());
    if (projectId != null) {
      count = publishLogRepository.count(projectId);
      raw = publishLogRepository.findPublishLogAll(projectId, pageable);
    } else {
      count = publishLogRepository.count();
      raw = publishLogRepository.findPublishLogAll(pageable);
    }
    return PublishLogShowVO.builder()
      .count(count)
      .data(raw.stream().map(p -> PublishLogVO.builder()
        .id(p.getId())
        .publishLogName(p.getPublishLogName())
        .projectName(p.getProjectName())
        .projectSourceType(p.getProjectSourceType())
        .userName(p.getUserName())
        .createAt(p.getCreateAt())
        .proxyScript(CommonUtil.getProxyScript(endpoint, bucketName, p.getProjectName(), p.getUuid(), p.getProjectSourceType()))
        .build()).collect(Collectors.toList()))
      .build();
  }

  @Override
  @Transactional
  public void addPublishLog(Long userId, CreatePublishLogAO params, MultipartFile file) {
    // 验证项目是否存在
    Optional<ProjectEntity> projectOptional = projectRepository.findById(params.getProjectId());
    if (!projectOptional.isPresent()) {
      throw new FrameMessageException("项目不存在");
    }
    // 生成随机上传码
    String uuid = CommonUtil.getDateUUID();
    ProjectEntity project = projectOptional.get();
    try {
      // 解压压缩包
      ZipArchiveInputStream zis = new ZipArchiveInputStream(file.getInputStream(), "GBK", true);
      ZipArchiveEntry entry = zis.getNextZipEntry();
      String firstDir = entry.getName();
      while (entry != null) {
        boolean isDir = !entry.isDirectory();
        String dirName = entry.getName();
        // minio文件上传
        if (isDir) {
          String objectName = project.getName() + "/" + uuid + "/" + dirName.replace(firstDir, "");
          minioUtil.putObject(bucketName, objectName, zis, entry.getSize());
        }
        entry = zis.getNextZipEntry();
      }
      zis.close();
    } catch (Exception error) {
      throw new FrameMessageException(error.toString());
    }
    // 保存日志
    publishLogRepository.save(PublishLogEntity.builder()
      .name(params.getName())
      .projectId(params.getProjectId())
      .uuid(uuid)
      .userId(userId)
      .build());
  }

  @Override
  @Transactional
  public void deletePublishLog(DeletePublishLogAO params) {
    // 验证项目发布是否存在
    Optional<PublishLogEntity> publishLogOptional = publishLogRepository.findById(params.getId());
    if (!publishLogOptional.isPresent()) {
      throw new FrameMessageException("项目发布不存在");
    }
    PublishLogEntity publishLog = publishLogOptional.get();
    Optional<ProjectEntity> projectOptional = projectRepository.findById(publishLog.getProjectId());
    if (!projectOptional.isPresent()) {
      throw new FrameMessageException("项目不存在");
    }
    ProjectEntity project = projectOptional.get();
    try {
      // minio项目删除
      minioUtil.batchRemoveObject(bucketName, project.getName() + "/" + publishLog.getUuid());
    } catch (Exception error) {
      throw new FrameMessageException(error.toString());
    }
    // 删除项目发布
    publishLog.setIsActive(false);
    publishLogRepository.save(publishLog);
  }

}