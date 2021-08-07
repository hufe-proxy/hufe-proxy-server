package com.hufe.frame.controller;

import com.hufe.frame.dataobject.ao.publishLog.CreatePublishLogAO;
import com.hufe.frame.dataobject.ao.publishLog.DeletePublishLogAO;
import com.hufe.frame.dataobject.ao.publishLog.SearchPublishLogAO;
import com.hufe.frame.dataobject.po.exception.FrameMessageException;
import com.hufe.frame.dataobject.vo.common.FrameResponse;
import com.hufe.frame.dataobject.vo.publishLog.PublishLogShowVO;
import com.hufe.frame.model.ProjectEntity;
import com.hufe.frame.service.impl.ProjectServiceImpl;
import com.hufe.frame.service.impl.PublishLogServiceImpl;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

@Api(tags = "2.项目发布")
@Validated
@RestController
@RequestMapping("/api/publish_log")
public class PublishLogController {

  @Autowired
  private PublishLogServiceImpl publishLogService;

  @Autowired
  private ProjectServiceImpl projectService;

  @ApiOperation(value = "获取项目发布列表")
  @ApiResponses({
          @ApiResponse(code = 401, message = "非法访问"),
          @ApiResponse(code = 422, message = "参数验证失败"),
          @ApiResponse(code = 500, message = "内部服务错误")
  })
  @PostMapping("/v1/list/by_search")
  @ResponseStatus(HttpStatus.OK)
  public FrameResponse<PublishLogShowVO> getList(@RequestBody @Validated SearchPublishLogAO inputAO, @RequestHeader("x-token") String header) {
    PublishLogShowVO result = publishLogService.getList(inputAO);
    return new FrameResponse<>(result);
  }

  @ApiOperation(value = "新增项目发布")
  @ApiResponses({
          @ApiResponse(code = 401, message = "非法访问"),
          @ApiResponse(code = 422, message = "参数验证失败"),
          @ApiResponse(code = 500, message = "内部服务错误")
  })
  @PostMapping("/v1/add")
  @ResponseStatus(HttpStatus.OK)
  public FrameResponse addPublishLog(@ApiParam(name = "name", value = "发布名称", required = true) @NotNull String name,
                                     @ApiParam(name = "projectId", value = "项目id", required = true) @NotNull Long projectId,
                                     @ApiParam(name = "attach", value = "项目压缩包", required = true) @NotNull MultipartFile attach,
                                     @RequestHeader("x-token") String header,
                                     HttpServletRequest request
  ) {
    Long userId = Long.parseLong((String) request.getAttribute("userId"));
    CreatePublishLogAO inputAO = CreatePublishLogAO.builder()
            .name(name)
            .projectId(projectId)
            .build();
    publishLogService.addPublishLog(userId, inputAO, attach);
    return new FrameResponse();
  }

  @ApiOperation(value = "新增项目发布通过CLI")
  @ApiResponses({
          @ApiResponse(code = 401, message = "非法访问"),
          @ApiResponse(code = 422, message = "参数验证失败"),
          @ApiResponse(code = 500, message = "内部服务错误")
  })
  @PostMapping("/v1/add/by_cli")
  @ResponseStatus(HttpStatus.OK)
  public FrameResponse<String> addPublishLogByCli(@ApiParam(name = "hostName", value = "主机名称", required = true) @NotNull String hostName,
                                                  @ApiParam(name = "hostVersion", value = "主机版本", required = true) @NotNull String hostVersion,
                                                  @ApiParam(name = "name", value = "发布名称", required = true) @NotNull String name,
                                                  @ApiParam(name = "projectName", value = "项目名称", required = true) @NotNull String projectName,
                                                  @ApiParam(name = "attach", value = "项目压缩包", required = true) @NotNull MultipartFile attach,
                                                  @RequestHeader("x-token") String header,
                                                  HttpServletRequest request
  ) {
    Long userId = Long.parseLong((String) request.getAttribute("userId"));
    ProjectEntity publishLog = projectService.findTop1ByNameContaining(projectName);
    if (publishLog == null) {
      throw new FrameMessageException("项目没有注册到平台");
    }
    CreatePublishLogAO inputAO = CreatePublishLogAO.builder()
            .hostName(hostName)
            .hostVersion(hostVersion)
            .name(name)
            .projectId(publishLog.getId())
            .build();
    String script = publishLogService.addPublishLog(userId, inputAO, attach);
    return new FrameResponse(script);
  }

  @ApiOperation(value = "删除项目发布")
  @ApiResponses({
          @ApiResponse(code = 401, message = "非法访问"),
          @ApiResponse(code = 422, message = "参数验证失败"),
          @ApiResponse(code = 500, message = "内部服务错误")
  })
  @PostMapping("/v1/delete")
  @ResponseStatus(HttpStatus.OK)
  public FrameResponse deletePublishLog(@RequestBody @Validated DeletePublishLogAO inputAO, @RequestHeader("x-token") String header) {
    publishLogService.deletePublishLog(inputAO);
    return new FrameResponse();
  }

}
