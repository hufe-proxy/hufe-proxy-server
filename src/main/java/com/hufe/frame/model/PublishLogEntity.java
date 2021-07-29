package com.hufe.frame.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "publish_log", indexes = {@Index(name = "IDX_PUB_PROJECT_ID", columnList = "projectId"), @Index(name = "IDX_PUB_USER_ID", columnList = "userId")})
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PublishLogEntity extends BaseEntity implements Serializable {

  // 发布名称
  private String name;

  // 上传随机码
  private String uuid;

  // 项目id
  private Long projectId;

  // 用户id
  private Long userId;

  // 发布主机名称
  private String hostName;

  // 发布主机版本
  private String hostVersion;

}
