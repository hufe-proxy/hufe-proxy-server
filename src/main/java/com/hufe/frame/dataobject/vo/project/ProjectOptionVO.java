package com.hufe.frame.dataobject.vo.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectOptionVO {

  @ApiModelProperty(value = "value")
  private Long value;

  @ApiModelProperty(value = "label")
  private String label;

}
