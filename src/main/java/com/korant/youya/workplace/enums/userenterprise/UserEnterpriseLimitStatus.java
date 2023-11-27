package com.korant.youya.workplace.enums.userenterprise;

/**
 * @Date 2023/11/24 16:07
 * @PackageName:com.korant.youya.workplace.enums.userenterprise
 * @ClassName: UserEnterpriseLimitStatus
 * @Description: TODO
 * @Version 1.0
 */
public enum UserEnterpriseLimitStatus {

    //审核中
    IN_PROGRESS(0),
    //未关联
    NOT_LIMIT(1),
    //已关联
    IS_LIMIT(2);

    private int status;

    UserEnterpriseLimitStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
