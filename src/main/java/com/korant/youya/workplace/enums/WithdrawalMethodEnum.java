package com.korant.youya.workplace.enums;

/**
 * @ClassName WithdrawalMethodEnum
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/6 11:32
 * @Version 1.0
 */
public enum WithdrawalMethodEnum {

    //支付宝账户
    ALIPAY_ACCOUNT(1),
    //微信钱包
    WECHAT_WALLET(2),
    //银行卡
    BANK_CARD(3);

    private int method;

    WithdrawalMethodEnum(int method) {
        this.method = method;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }
}
