package com.korant.youya.workplace.enums;

/**
 * @ClassName TransactionFlowStatusEnum
 * @Description
 * @Author chenyiqiang
 * @Date 2024/2/29 15:25
 * @Version 1.0
 */
public enum TransactionFlowStatusEnum {

    //待处理，交易已发起但尚未确认
    PENDING(1),
    //处理中，系统正在验证和执行交易
    PROCESSING(2),
    //已支付，交易成功完成，款项已划转
    PAID(3),
    //支付失败，由于某些原因交易未能完成
    FAILED(4),
    //已退款，原始交易金额部分或全部退还给付款方
    REFUNDED(5),
    //已取消，交易在完成前被用户或系统取消
    CANCELLED(6),
    //结算完成，资金清算完毕，已入账
    SETTLED(7),
    //交易冲正，之前的支付状态被反向操作
    REVERSED(8),
    //已过期，交易在有效期内未完成
    EXPIRED(9),
    //部分支付，订单的一部分金额已经支付
    PARTIALLY_PAID(10);

    private int status;

    TransactionFlowStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
