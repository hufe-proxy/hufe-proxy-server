
package com.hufe.frame.service.impl;

import com.hufe.frame.dataobject.ao.publishLog.CreatePublishLogAO;
import com.hufe.frame.dataobject.ao.publishLog.DeletePublishLogAO;
import com.hufe.frame.dataobject.ao.publishLog.SearchPublishLogAO;
import com.hufe.frame.dataobject.bo.PublishLogShowBO;
import com.hufe.frame.dataobject.bo.SendNoticeAtBO;
import com.hufe.frame.dataobject.bo.SendNoticeBO;
import com.hufe.frame.dataobject.bo.SendNoticeTextBO;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PublishLogServiceImpl implements PublishLogService {

  @Value("${minio.endpoint}")
  private String endpoint;

  @Value("${minio.bucketName}")
  private String bucketName;

  @Value("${notice.uri}")
  private String noticeUri;

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
                    .proxyScript(CommonUtil.getPublishLogProxyScript(endpoint, bucketName, p.getProjectName(), p.getUuid()))
                    .build()).collect(Collectors.toList()))
            .build();
  }

  @Override
  @Transactional
  public String addPublishLog(Long userId, CreatePublishLogAO params, MultipartFile file) {
    // ????????????????????????
    Optional<ProjectEntity> projectOptional = projectRepository.findById(params.getProjectId());
    if (!projectOptional.isPresent()) {
      throw new FrameMessageException("???????????????");
    }
    // ?????????????????????
    String uuid = CommonUtil.getDateUUID();
    ProjectEntity project = projectOptional.get();
    try {
      // ???????????????
      ZipArchiveInputStream zis = new ZipArchiveInputStream(file.getInputStream(), "GBK", true);
      ZipArchiveEntry entry = zis.getNextZipEntry();
      // ????????????
      String firstRoute = entry.getName();
      Pattern p = Pattern.compile("([^/]+/).*");
      Matcher m = p.matcher(firstRoute);
      if (!m.find()) {
        throw new FrameMessageException("???????????????????????????");
      }
      String firstDir = m.group(1);
      while (entry != null) {
        boolean isFile = !entry.isDirectory();
        String dirName = entry.getName();
        // minio????????????
        if (isFile) {
          String objectName = project.getName() + "/" + uuid + "/" + dirName.replace(firstDir, "");
          long size = entry.getSize();
          if (size == -1) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while (true) {
              int bytes = zis.read();
              if (bytes == -1) break;
              bos.write(bytes);
            }
            bos.close();
            InputStream is = new ByteArrayInputStream(bos.toByteArray());
            minioUtil.putObject(bucketName, objectName, is, is.available());
          } else {
            minioUtil.putObject(bucketName, objectName, zis, size);
          }
        }
        entry = zis.getNextZipEntry();
      }
      zis.close();
    } catch (Exception error) {
      throw new FrameMessageException(error.toString());
    }
    // ????????????
    publishLogRepository.save(PublishLogEntity.builder()
            .hostName(params.getHostName())
            .hostVersion(params.getHostVersion())
            .name(params.getName())
            .projectId(project.getId())
            .uuid(uuid)
            .userId(userId)
            .build());
    return CommonUtil.getPublishLogProxyScript(endpoint, bucketName, project.getName(), uuid);
  }

  @Override
  @Async
  public void sendNotice(String projectName, CreatePublishLogAO params) {
    try {
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.setContentType(MediaType.APPLICATION_JSON);
      // ????????????
      DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String now = format.format(new Date());
      SendNoticeBO sendNotice = SendNoticeBO.builder()
              .msgtype("text")
              .text(SendNoticeTextBO.builder()
                      .content(projectName
                              + "??????????????????\n???????????????"
                              + params.getName()
                              + "\n???????????????"
                              + params.getHostName()
                              + "\n???????????????"
                              + now
                              + "\n???????????????WiNEX")
                      .build())
              .at(SendNoticeAtBO.builder().isAtAll(true).build())
              .build();
      HttpEntity<SendNoticeBO> entity = new HttpEntity<>(sendNotice, httpHeaders);
      restTemplate.postForEntity(noticeUri, entity, String.class);
    } catch (Exception exception) {
      log.error("???????????????????????????" + exception.toString());
    }
  }

  @Override
  @Transactional
  public void deletePublishLog(DeletePublishLogAO params) {
    // ??????????????????????????????
    Optional<PublishLogEntity> publishLogOptional = publishLogRepository.findById(params.getId());
    if (!publishLogOptional.isPresent()) {
      throw new FrameMessageException("?????????????????????");
    }
    PublishLogEntity publishLog = publishLogOptional.get();
    Optional<ProjectEntity> projectOptional = projectRepository.findById(publishLog.getProjectId());
    if (!projectOptional.isPresent()) {
      throw new FrameMessageException("???????????????");
    }
    ProjectEntity project = projectOptional.get();
    try {
      // minio????????????
      minioUtil.batchRemoveObject(bucketName, project.getName() + "/" + publishLog.getUuid());
    } catch (Exception error) {
      throw new FrameMessageException(error.toString());
    }
    // ??????????????????
    publishLog.setIsActive(false);
    publishLogRepository.save(publishLog);
  }

}
