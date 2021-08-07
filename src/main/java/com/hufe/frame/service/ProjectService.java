package com.hufe.frame.service;

import com.hufe.frame.dataobject.ao.project.CreateProjectAO;
import com.hufe.frame.dataobject.ao.project.DeleteProjectAO;
import com.hufe.frame.dataobject.ao.project.SearchProjectAO;
import com.hufe.frame.dataobject.ao.project.UpdateProjectAO;
import com.hufe.frame.dataobject.vo.project.ProjectOptionVO;
import com.hufe.frame.dataobject.vo.project.ProjectShowVO;
import com.hufe.frame.model.ProjectEntity;

import java.util.List;

public interface ProjectService {

  ProjectShowVO getList(SearchProjectAO params);

  List<ProjectOptionVO> getAll();

  void addProject(CreateProjectAO params) throws Exception;

  void deleteProject(DeleteProjectAO params);

  void updateProject(UpdateProjectAO params);

  ProjectEntity findTop1ByNameContaining(String name);

}
