package com.hufe.frame.dataobject.vo.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginShowVO {

    @ApiModelProperty(value = "token")
    private String token;

    @ApiModelProperty(value = "角色类型")
    private Integer roleType;

}
