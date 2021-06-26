package com.hufe.frame.dataobject.ao.publishLog;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class DeletePublishLogAO {

  @ApiModelProperty(value = "id", required = true)
  @NotNull
  private Long id;

}
