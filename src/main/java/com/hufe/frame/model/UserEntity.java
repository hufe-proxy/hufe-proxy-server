package com.hufe.frame.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "user", indexes = {@Index(name = "IDX_USER_ACCOUNT", columnList = "account")})
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity implements Serializable {

  // 姓名
  private String name;

  // 账号
  private String account;

  // 密码
  private String password;

  // 用户角色权限-超级管理员，其他管理者
  private Integer roleType;

}
