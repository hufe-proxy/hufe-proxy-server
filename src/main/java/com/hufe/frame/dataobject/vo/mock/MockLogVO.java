package com.hufe.frame.dataobject.vo.mock;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class MockLogVO {

  @ApiModelProperty(value = "id")
  private Long id;

  @ApiModelProperty(value = "代理地址")
  private String address;

  @ApiModelProperty(value = "代理备注")
  private String remark;

  @ApiModelProperty(value = "代理内容")
  private String content;

  @ApiModelProperty(value = "创建日期")
  private Date createAt;

  @ApiModelProperty(value = "代理脚本")
  private String proxyScript;

}
