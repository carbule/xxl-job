package com.korant.youya.workplace.enums.user;

/**
 * @ClassName JobTypeEnum
 * @Description
 * @Author chenyiqiang
 * @Date 2023/8/16 9:34
 * @Version 1.0
 */
public enum UserAuthenticationStatusEnum {

    //未认证
    NOT_CERTIFIED(0),
    //已认证
    CERTIFIED(1);

    private int status;

    UserAuthenticationStatusEnum(int type) {
        this.status = type;
    }

    public int getType() {
        return status;
    }

    public void setType(int type) {
        this.status = type;
    }
}
