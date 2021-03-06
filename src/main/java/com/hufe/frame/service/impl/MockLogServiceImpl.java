package com.hufe.frame.service.impl;

import com.hufe.frame.dataobject.ao.mock.CreateMockLogAO;
import com.hufe.frame.dataobject.ao.mock.DeleteMockLogAO;
import com.hufe.frame.dataobject.ao.mock.SearchMockLogAO;
import com.hufe.frame.dataobject.ao.mock.UpdateMockLogAO;
import com.hufe.frame.dataobject.po.exception.FrameMessageException;
import com.hufe.frame.dataobject.vo.mock.MockLogShowVO;
import com.hufe.frame.dataobject.vo.mock.MockLogVO;
import com.hufe.frame.model.MockLogEntity;
import com.hufe.frame.repository.MockLogRepository;
import com.hufe.frame.service.MockLogService;
import com.hufe.frame.util.CommonUtil;
import com.hufe.frame.util.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.*;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MockLogServiceImpl implements MockLogService {

  @Value("${mock.log.endpoint}")
  private String endpoint;

  @Value("${mock.log.dir}")
  private String logDir;

  @Autowired
  private MinioUtil minioUtil;

  @Autowired
  private MockLogRepository mockLogRepository;

  @Override
  public MockLogShowVO getList(SearchMockLogAO params) {
    long count;
    Page<MockLogEntity> raw = null;
    String keyword = params.getKeyword();
    Pageable pageable = PageRequest.of(params.getPageNo(), params.getPageSize());
    if (!"".equals(keyword) && keyword != null) {
      count = mockLogRepository.countByAddressContainingAndIsActiveTrue(keyword);
      raw = mockLogRepository.findByAddressContainingAndIsActiveTrueOrderByIdDesc(keyword, pageable);
    } else {
      count = mockLogRepository.countByIsActiveTrue();
      raw = mockLogRepository.findByIsActiveTrueOrderByIdDesc(pageable);
    }
    return MockLogShowVO.builder()
            .count(count)
            .data(raw.stream().map(r -> MockLogVO.builder()
                    .id(r.getId())
                    .address(r.getAddress())
                    .remark(r.getRemark())
                    .content(r.getContent())
                    .createAt(r.getCreateAt())
                    .proxyScript(CommonUtil.getMockLogProxyScript(endpoint, r.getName(), r.getAddress()))
                    .build()).collect(Collectors.toList()))
            .build();
  }

  @Override
  @Transactional
  public void addMock(Long userId, CreateMockLogAO params) {
    // name
    String name = CommonUtil.getDateUUID() + ".json";
    // ??????mock??????
    mockLogRepository.save(MockLogEntity.builder()
            .name(name)
            .address(params.getAddress())
            .content(params.getContent())
            .remark(params.getRemark())
            .userId(userId)
            .build());
    // ????????????????????????
    try {
      File file = new File(logDir + "/" + name);
      FileOutputStream fos = new FileOutputStream(file);
      byte[] jsonStr = params.getContent().getBytes();
      fos.write(jsonStr);
      fos.close();
    } catch (Exception error) {
      throw new FrameMessageException(error.toString());
    }
  }

  @Override
  @Transactional
  public void deleteMock(DeleteMockLogAO params) {
    // ??????mock????????????
    Optional<MockLogEntity> mockLogOptional = mockLogRepository.findById(params.getId());
    if (!mockLogOptional.isPresent()) {
      throw new FrameMessageException("mock?????????");
    }
    // ?????????????????????
    MockLogEntity mockLog = mockLogOptional.get();
    mockLog.setIsActive(false);
    mockLogRepository.save(mockLog);
    // ??????minio??????
    try {
      File file = new File(logDir + "/" + mockLog.getName());
      file.delete();
    } catch (Exception error) {
      throw new FrameMessageException(error.toString());
    }
  }

  @Override
  @Transactional
  public void updateMock(UpdateMockLogAO params) {
    // ??????mock????????????
    Optional<MockLogEntity> mockLogOptional = mockLogRepository.findById(params.getId());
    if (!mockLogOptional.isPresent()) {
      throw new FrameMessageException("mock?????????");
    }
    MockLogEntity mockLog = mockLogOptional.get();
    String content = mockLog.getContent();
    // ???????????????mock??????
    mockLog.setAddress(params.getAddress());
    mockLog.setRemark(params.getRemark());
    mockLog.setContent(params.getContent());
    mockLogRepository.save(mockLog);
    // ??????minio??????
    if (!content.equals(params.getContent())) {
      try {
        String name = mockLog.getName();
        File file = new File(logDir + "/" + mockLog.getName());
        FileOutputStream fos = new FileOutputStream(file);
        byte[] jsonStr = params.getContent().getBytes();
        fos.write(jsonStr);
        fos.close();
      } catch (Exception error) {
        throw new FrameMessageException(error.toString());
      }
    }
  }

}
