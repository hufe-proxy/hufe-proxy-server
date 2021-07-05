package com.hufe.frame.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mock_log")
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class MockLogEntity extends BaseEntity implements Serializable {

  // 名称
  private String name;

  // 代理地址
  private String address;

  // 代理备注
  private String remark;

  // 代理内容
  @Column(columnDefinition = "TEXT")
  private String content;

  // 用户id
  private Long userId;

}

