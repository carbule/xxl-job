package com.korant.youya.workplace.enums.user;

/**
 * @ClassName UserAccountStatus
 * @Description
 * @Author chenyiqiang
 * @Date 2023/9/6 10:49
 * @Version 1.0
 */
public enum UserAccountStatusEnum {

    //未冻结
    UNFROZEN(0),
    //已冻结
    FROZEN(1);

    private int status;

    UserAccountStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
