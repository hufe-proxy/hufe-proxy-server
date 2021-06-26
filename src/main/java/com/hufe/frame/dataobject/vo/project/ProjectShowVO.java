package com.hufe.frame.dataobject.vo.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProjectShowVO {

    @ApiModelProperty(value = "总数")
    private Long count;

    @ApiModelProperty(value = "访问列表")
    private List<ProjectVO> data;

}
