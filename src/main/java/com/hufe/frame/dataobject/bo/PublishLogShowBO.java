package com.hufe.frame.dataobject.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class PublishLogShowBO {

  @ApiModelProperty(value = "id")
  private Long id;

  @ApiModelProperty(value = "发布名称")
  private String publishLogName;

  @ApiModelProperty(value = "发布码")
  private String uuid;

  @ApiModelProperty(value = "项目名称")
  private String projectName;

  @ApiModelProperty(value = "项目来源")
  private Integer projectSourceType;

  @ApiModelProperty(value = "发布人")
  private String userName;

  @ApiModelProperty(value = "创建日期")
  private Date createAt;

}
