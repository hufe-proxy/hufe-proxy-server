package com.hufe.frame.constant.user;

public enum UserRoleTypeEnum {

    SuperAdmin(0),
    Other(1);

    private int value;

    UserRoleTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
