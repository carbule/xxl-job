package com.korant.youya.workplace.enums.enterprisetodo;

/**
 * @Date 2023/11/22 10:01
 * @PackageName:com.korant.youya.workplace.enums.enterprisetodo
 * @ClassName: EnterpriseTodoOperateStatus
 * @Description:
 * @Version 1.0
 */
public enum EnterpriseTodoOperateStatus {

    //待审核
    OPERATE_IN_PROGRESS(0),
    //拒绝
    OPERATE_FAIL(1),
    //同意
    OPERATE_SUCCESS(2);

    private int status;

    EnterpriseTodoOperateStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
