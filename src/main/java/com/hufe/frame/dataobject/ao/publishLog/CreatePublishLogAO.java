package com.hufe.frame.dataobject.ao.publishLog;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class CreatePublishLogAO {

  @ApiModelProperty(value = "主机名称")
  private String hostName;

  @ApiModelProperty(value = "主机版本")
  private String hostVersion;

  @ApiModelProperty(value = "发布名称", required = true)
  @NotNull
  private String name;

  @ApiModelProperty(value = "项目id", required = true)
  @NotNull
  private Long projectId;

}
