package com.hufe.frame.dataobject.vo.publishLog;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PublishLogShowVO {

  @ApiModelProperty(value = "总数")
  private Long count;

  @ApiModelProperty(value = "项目发布列表")
  private List<PublishLogVO> data;

}
