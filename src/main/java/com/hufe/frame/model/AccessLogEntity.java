package com.hufe.frame.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "access_log")
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AccessLogEntity extends BaseEntity implements Serializable {

  // 用户id
  private String userId;

  // api地址
  private String api;

  // ip地址
  private String ip;

  // 位置信息
  private String location;

  // 设备信息
  private String device;

  // 设备地址mac
  private String mac;

  // get信息
  private String query;

  // post信息
  private String request;

}
