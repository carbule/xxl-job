package com.korant.youya.workplace.enums.enterprise;

/**
 * @ClassName EnterpriseAuthStatusEnum
 * @Description
 * @Author chenyiqiang
 * @Date 2023/8/1 15:46
 * @Version 1.0
 */
public enum EnterpriseAuthStatusEnum {

    //审核中
    AUTH_IN_PROGRESS(0),
    //审核失败
    AUTH_FAIL(1),
    //审核成功
    AUTH_SUCCESS(2);

    private int status;

    EnterpriseAuthStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
