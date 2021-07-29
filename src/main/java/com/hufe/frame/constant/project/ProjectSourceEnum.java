package com.hufe.frame.constant.project;

public enum ProjectSourceEnum {

  Outpatient(0),
  Inpatient(1),
  Other(2);

  private int value;

  ProjectSourceEnum(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
