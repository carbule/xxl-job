package com.korant.youya.workplace.enums.user;

/**
 * @ClassName UserAuthenticationStatusEnum
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

    UserAuthenticationStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
