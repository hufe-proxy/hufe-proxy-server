package com.hufe.frame.dataobject.ao.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class LoginAO {

  @ApiModelProperty(value = "账号", required = true)
  @NotNull
  private String account;

  @ApiModelProperty(value = "密码", required = true)
  @NotNull
  private String password;

}
