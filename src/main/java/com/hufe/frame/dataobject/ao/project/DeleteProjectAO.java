package com.hufe.frame.dataobject.ao.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class DeleteProjectAO {

    @ApiModelProperty(value = "id", required = true)
    @NotNull
    private Long id;

}
