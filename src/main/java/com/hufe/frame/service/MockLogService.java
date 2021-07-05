package com.hufe.frame.service;

import com.hufe.frame.dataobject.ao.mock.CreateMockLogAO;
import com.hufe.frame.dataobject.ao.mock.DeleteMockLogAO;
import com.hufe.frame.dataobject.ao.mock.SearchMockLogAO;
import com.hufe.frame.dataobject.ao.mock.UpdateMockLogAO;
import com.hufe.frame.dataobject.vo.mock.MockLogShowVO;

import java.io.FileNotFoundException;

public interface MockLogService {

  MockLogShowVO getList(SearchMockLogAO params);

  void addMock(Long userId, CreateMockLogAO params) throws FileNotFoundException;

  void deleteMock(DeleteMockLogAO params);

  void updateMock(UpdateMockLogAO params);

}
