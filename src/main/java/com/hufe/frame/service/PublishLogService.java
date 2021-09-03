package com.hufe.frame.service;

import com.hufe.frame.dataobject.ao.publishLog.CreatePublishLogAO;
import com.hufe.frame.dataobject.ao.publishLog.DeletePublishLogAO;
import com.hufe.frame.dataobject.ao.publishLog.SearchPublishLogAO;
import com.hufe.frame.dataobject.vo.publishLog.PublishLogShowVO;
import org.springframework.web.multipart.MultipartFile;

public interface PublishLogService {

  PublishLogShowVO getList(SearchPublishLogAO params);

  String addPublishLog(Long userId, CreatePublishLogAO params, MultipartFile attach);

  void sendNotice(String projectName, CreatePublishLogAO params);

  void deletePublishLog(DeletePublishLogAO params);

}
