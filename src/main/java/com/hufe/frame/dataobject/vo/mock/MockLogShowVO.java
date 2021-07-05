package com.hufe.frame.dataobject.vo.mock;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MockLogShowVO {

  @ApiModelProperty(value = "总数")
  private Long count;

  @ApiModelProperty(value = "mock列表")
  private List<MockLogVO> data;

}
