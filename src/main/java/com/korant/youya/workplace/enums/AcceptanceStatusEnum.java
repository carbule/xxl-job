package com.korant.youya.workplace.enums;

/**
 * @ClassName AcceptanceStatusEnum
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/10 11:26
 * @Version 1.0
 */
public enum AcceptanceStatusEnum {

    //等待接受
    PENDING(0),
    //已拒绝
    REJECTED(1),
    //已接受
    ACCEPTED(2);

    private int status;

    AcceptanceStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
