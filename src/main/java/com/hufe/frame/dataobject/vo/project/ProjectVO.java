package com.hufe.frame.dataobject.vo.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ProjectVO {

  @ApiModelProperty(value = "id")
  private Long id;

  @ApiModelProperty(value = "项目名称")
  private String name;

  @ApiModelProperty(value = "项目来源")
  private Integer sourceType;

  @ApiModelProperty(value = "创建日期")
  private Date createAt;

}
