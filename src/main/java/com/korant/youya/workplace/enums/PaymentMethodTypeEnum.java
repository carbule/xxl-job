package com.korant.youya.workplace.enums;

/**
 * @ClassName PaymentMethodTypeEnum
 * @Description
 * @Author chenyiqiang
 * @Date 2024/2/29 16:07
 * @Version 1.0
 */
public enum PaymentMethodTypeEnum {

    WECHAT_PAYMENT(1),
    ALIPAY_PAYMENT(2),
    UNIONPAY_PAYMENT(3);

    private int type;

    PaymentMethodTypeEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
