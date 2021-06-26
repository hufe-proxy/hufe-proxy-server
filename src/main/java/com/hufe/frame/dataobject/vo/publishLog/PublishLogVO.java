package com.hufe.frame.dataobject.vo.publishLog;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class PublishLogVO {

  @ApiModelProperty(value = "id")
  private Long id;

  @ApiModelProperty(value = "发布名称")
  private String publishLogName;

  @ApiModelProperty(value = "项目名称")
  private String projectName;

  @ApiModelProperty(value = "项目来源")
  private Integer projectSourceType;

  @ApiModelProperty(value = "发布人")
  private String userName;

  @ApiModelProperty(value = "创建日期")
  private Date createAt;

  @ApiModelProperty(value = "代理脚本")
  private String proxyScript;

}
