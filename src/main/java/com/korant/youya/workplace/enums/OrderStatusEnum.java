package com.korant.youya.workplace.enums;

/**
 * @ClassName OrderStatusEnum
 * @Description
 * @Author chenyiqiang
 * @Date 2024/2/29 15:33
 * @Version 1.0
 */
public enum OrderStatusEnum {

    //待支付
    PENDING_PAYMENT(1),
    //支付处理中
    PROCESSING_PAYMENT(2),
    //支付成功
    PAID(3),
    //支付失败
    PAYMENT_FAILED(4),
    //支付超时
    PAYMENT_TIMEOUT(5),
    //支付取消
    PAYMENT_CANCELED(6),
    //退款中
    REFUNDING(7),
    //已退款
    REFUNDED(8),
    //已关闭
    CLOSED(9);

    private int status;

    OrderStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
