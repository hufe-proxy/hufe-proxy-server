package com.hufe.frame.dataobject.ao.mock;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class UpdateMockLogAO extends CreateMockLogAO {

  @ApiModelProperty(value = "id", required = true)
  @NotNull
  private Long id;

}
