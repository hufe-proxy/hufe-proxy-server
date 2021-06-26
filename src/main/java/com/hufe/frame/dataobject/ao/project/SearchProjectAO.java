package com.hufe.frame.dataobject.ao.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
public class SearchProjectAO {

    @ApiModelProperty(value = "关键词")
    private String keyword;

    @ApiModelProperty(value = "当前页", required = true)
    @NotNull
    private int pageNo;

    @ApiModelProperty(value = "分页大小", required = true)
    @NotNull
    @Min(1)
    private int pageSize;

}
