package com.hufe.frame.controller;

import com.hufe.frame.dataobject.ao.project.CreateProjectAO;
import com.hufe.frame.dataobject.ao.project.DeleteProjectAO;
import com.hufe.frame.dataobject.ao.project.SearchProjectAO;
import com.hufe.frame.dataobject.ao.project.UpdateProjectAO;
import com.hufe.frame.dataobject.vo.common.FrameResponse;
import com.hufe.frame.dataobject.vo.project.ProjectOptionVO;
import com.hufe.frame.dataobject.vo.project.ProjectShowVO;
import com.hufe.frame.service.impl.ProjectServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "1.项目维护")
@Validated
@RestController
@RequestMapping("/api/project")
public class ProjectController {

  @Autowired
  private ProjectServiceImpl projectService;

  @ApiOperation(value = "获取项目列表")
  @ApiResponses({
    @ApiResponse(code = 401, message = "非法访问"),
    @ApiResponse(code = 422, message = "参数验证失败"),
    @ApiResponse(code = 500, message = "内部服务错误")
  })
  @PostMapping("/v1/list/by_search")
  @ResponseStatus(HttpStatus.OK)
  public FrameResponse<ProjectShowVO> getList(@RequestBody @Validated SearchProjectAO inputAO, @RequestHeader("x-token") String header) {
    ProjectShowVO result = projectService.getList(inputAO);
    return new FrameResponse<>(result);
  }

  @ApiOperation(value = "获取所有项目")
  @ApiResponses({
    @ApiResponse(code = 401, message = "非法访问"),
    @ApiResponse(code = 422, message = "参数验证失败"),
    @ApiResponse(code = 500, message = "内部服务错误")
  })
  @PostMapping("/v1/list/by_example")
  @ResponseStatus(HttpStatus.OK)
  public FrameResponse<List<ProjectOptionVO>> getAll(@RequestHeader("x-token") String header) {
    List<ProjectOptionVO> result = projectService.getAll();
    return new FrameResponse<>(result);
  }

  @ApiOperation(value = "新增项目")
  @ApiResponses({
    @ApiResponse(code = 401, message = "非法访问"),
    @ApiResponse(code = 422, message = "参数验证失败"),
    @ApiResponse(code = 500, message = "内部服务错误")
  })
  @PostMapping("/v1/add")
  @ResponseStatus(HttpStatus.OK)
  public FrameResponse addProject(@RequestBody @Validated CreateProjectAO inputAO, @RequestHeader("x-token") String header) {
    projectService.addProject(inputAO);
    return new FrameResponse();
  }

  @ApiOperation(value = "删除项目")
  @ApiResponses({
    @ApiResponse(code = 401, message = "非法访问"),
    @ApiResponse(code = 422, message = "参数验证失败"),
    @ApiResponse(code = 500, message = "内部服务错误")
  })
  @PostMapping("/v1/delete")
  @ResponseStatus(HttpStatus.OK)
  public FrameResponse deleteProject(@RequestBody @Validated DeleteProjectAO inputAO, @RequestHeader("x-token") String header) {
    projectService.deleteProject(inputAO);
    return new FrameResponse();
  }

  @ApiOperation(value = "更新项目")
  @ApiResponses({
    @ApiResponse(code = 401, message = "非法访问"),
    @ApiResponse(code = 422, message = "参数验证失败"),
    @ApiResponse(code = 500, message = "内部服务错误")
  })
  @PostMapping("/v1/update")
  @ResponseStatus(HttpStatus.OK)
  public FrameResponse updateProject(@RequestBody @Validated UpdateProjectAO inputAO, @RequestHeader("x-token") String header) {
    projectService.updateProject(inputAO);
    return new FrameResponse();
  }

}
