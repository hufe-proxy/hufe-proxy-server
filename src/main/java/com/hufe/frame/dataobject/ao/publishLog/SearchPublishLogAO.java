package com.hufe.frame.dataobject.ao.publishLog;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
public class SearchPublishLogAO {

  @ApiModelProperty(value = "项目id")
  private Long projectId;

  @ApiModelProperty(value = "当前页", required = true)
  @NotNull
  private int pageNo;

  @ApiModelProperty(value = "分页大小", required = true)
  @NotNull
  @Min(1)
  private int pageSize;

}
