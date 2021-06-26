package com.hufe.frame.dataobject.ao.project;

import com.hufe.frame.constant.project.ProjectSourceEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class CreateProjectAO {

    @ApiModelProperty(value = "项目名称", required = true)
    @NotNull
    private String name;

    @ApiModelProperty(value = "项目来源", required = true)
    @NotNull
    private ProjectSourceEnum sourceType;

}
