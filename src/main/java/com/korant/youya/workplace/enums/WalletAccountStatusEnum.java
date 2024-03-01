package com.korant.youya.workplace.enums;

/**
 * @ClassName WalletAccountStatusEnum
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/1 10:46
 * @Version 1.0
 */
public enum WalletAccountStatusEnum {

    //未冻结
    UNFROZEN(0),
    //已冻结
    FROZEN(1);

    private int status;

    WalletAccountStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
