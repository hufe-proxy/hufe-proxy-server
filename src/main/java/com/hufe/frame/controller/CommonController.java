package com.hufe.frame.controller;

import com.hufe.frame.dataobject.ao.common.LoginAO;
import com.hufe.frame.dataobject.vo.common.FrameResponse;
import com.hufe.frame.dataobject.vo.common.LoginShowVO;
import com.hufe.frame.service.impl.CommonServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "0.通用")
@Validated
@RestController
@RequestMapping("/api/common")
public class CommonController {

    @Autowired
    private CommonServiceImpl commonService;

    @ApiOperation(value = "用户登录")
    @ApiResponses({
            @ApiResponse(code = 401, message = "非法访问"),
            @ApiResponse(code = 422, message = "参数验证失败"),
            @ApiResponse(code = 500, message = "内部服务错误")
    })
    @PostMapping("/v1/login")
    @ResponseStatus(HttpStatus.OK)
    public FrameResponse<LoginShowVO> getUserVisitList(@RequestBody @Validated LoginAO inputAO) {
        LoginShowVO result = commonService.login(inputAO);
        return new FrameResponse<>(result);
    }

}
