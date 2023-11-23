package com.korant.youya.workplace.enums.huntjob;

/**
 * @ClassName HuntJobAuditStatusEnum
 * @Description
 * @Author chenyiqiang
 * @Date 2023/9/6 10:49
 * @Version 1.0
 */
public enum HuntJobAuditStatusEnum {

    //未审核
    UNAUDITED(0),
    //审核失败
    AUDIT_FAILED(1),
    //审核成功
    AUDIT_SUCCESS(2);

    private int status;

    HuntJobAuditStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
