package com.korant.youya.workplace.enums.job;

/**
 * @ClassName JobAuditStatusEnum
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/29 17:52
 * @Version 1.0
 */
public enum JobAuditStatusEnum {

    //未审核
    UNAUDITED(0),
    //审核失败
    AUDIT_FAILED(1),
    //审核成功
    AUDIT_SUCCESS(2);

    private int status;

    JobAuditStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
