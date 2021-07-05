package com.hufe.frame.dataobject.ao.mock;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class CreateMockLogAO {

  @ApiModelProperty(value = "代理地址", required = true)
  @NotNull
  private String address;

  @ApiModelProperty(value = "代理备注", required = true)
  @NotNull
  private String remark;

  @ApiModelProperty(value = "代理内容", required = true)
  @NotNull
  private String content;

}
