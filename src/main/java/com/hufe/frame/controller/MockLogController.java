package com.hufe.frame.controller;

import com.hufe.frame.dataobject.ao.mock.CreateMockLogAO;
import com.hufe.frame.dataobject.ao.mock.DeleteMockLogAO;
import com.hufe.frame.dataobject.ao.mock.SearchMockLogAO;
import com.hufe.frame.dataobject.ao.mock.UpdateMockLogAO;
import com.hufe.frame.dataobject.vo.common.FrameResponse;
import com.hufe.frame.dataobject.vo.mock.MockLogShowVO;
import com.hufe.frame.service.impl.MockLogServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "3.mock数据")
@Validated
@RestController
@RequestMapping("/api/mock_log")
public class MockLogController {

  @Autowired
  private MockLogServiceImpl mockService;

  @ApiOperation(value = "获取mock数据列表")
  @ApiResponses({
          @ApiResponse(code = 401, message = "非法访问"),
          @ApiResponse(code = 422, message = "参数验证失败"),
          @ApiResponse(code = 500, message = "内部服务错误")
  })
  @PostMapping("/v1/list/by_search")
  @ResponseStatus(HttpStatus.OK)
  public FrameResponse<MockLogShowVO> getList(@RequestBody @Validated SearchMockLogAO inputAO, @RequestHeader("x-token") String header) {
    MockLogShowVO result = mockService.getList(inputAO);
    return new FrameResponse<>(result);
  }

  @ApiOperation(value = "新增mock数据")
  @ApiResponses({
          @ApiResponse(code = 401, message = "非法访问"),
          @ApiResponse(code = 422, message = "参数验证失败"),
          @ApiResponse(code = 500, message = "内部服务错误")
  })
  @PostMapping("/v1/add")
  @ResponseStatus(HttpStatus.OK)
  public FrameResponse addMock(@RequestBody @Validated CreateMockLogAO inputAO, @RequestHeader("x-token") String header, HttpServletRequest request) {
    Long userId = Long.parseLong((String) request.getAttribute("userId"));
    mockService.addMock(userId, inputAO);
    return new FrameResponse();
  }

  @ApiOperation(value = "删除mock数据")
  @ApiResponses({
          @ApiResponse(code = 401, message = "非法访问"),
          @ApiResponse(code = 422, message = "参数验证失败"),
          @ApiResponse(code = 500, message = "内部服务错误")
  })
  @PostMapping("/v1/delete")
  @ResponseStatus(HttpStatus.OK)
  public FrameResponse deleteMock(@RequestBody @Validated DeleteMockLogAO inputAO, @RequestHeader("x-token") String header) {
    mockService.deleteMock(inputAO);
    return new FrameResponse();
  }

  @ApiOperation(value = "更新mock数据")
  @ApiResponses({
          @ApiResponse(code = 401, message = "非法访问"),
          @ApiResponse(code = 422, message = "参数验证失败"),
          @ApiResponse(code = 500, message = "内部服务错误")
  })
  @PostMapping("/v1/update")
  @ResponseStatus(HttpStatus.OK)
  public FrameResponse updateMock(@RequestBody @Validated UpdateMockLogAO inputAO, @RequestHeader("x-token") String header) {
    mockService.updateMock(inputAO);
    return new FrameResponse();
  }

}
