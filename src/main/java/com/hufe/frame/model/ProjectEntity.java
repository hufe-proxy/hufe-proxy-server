package com.hufe.frame.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "project", indexes = {@Index(name = "IDX_PROJECT_NAME", columnList = "name")})
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProjectEntity extends BaseEntity implements Serializable {

  // 名称
  private String name;

  // 项目来源：住院、门诊
  private Integer sourceType;

}
