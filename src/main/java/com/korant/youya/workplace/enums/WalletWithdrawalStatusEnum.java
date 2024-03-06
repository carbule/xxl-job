package com.korant.youya.workplace.enums;

/**
 * @ClassName WalletWithdrawalStatusEnum
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/6 11:35
 * @Version 1.0
 */
public enum WalletWithdrawalStatusEnum {

    //提现处理中
    PROCESSING(1),
    //提现成功
    SUCCESSFUL(2),
    //提现失败
    FAILED(3);

    private int status;

    WalletWithdrawalStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
