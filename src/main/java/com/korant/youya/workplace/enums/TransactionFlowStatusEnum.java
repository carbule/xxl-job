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
    PENDING(1, "交易待处理"),
    //处理中，系统正在验证和执行交易
    PROCESSING(2, "交易处理中"),
    //交易成功，款项已划转
    SUCCESSFUL(3, "交易成功"),
    //交易失败，由于某些原因交易未能完成
    FAILED(4, "交易失败"),
    //已过期，交易在有效期内未完成,
    EXPIRED(5, "交易已过期"),
    //已取消，交易在完成前被用户或系统取消
    CANCELLED(6, "交易已取消"),
    //已退款，原始交易金额部分或全部退还给付款方
    REFUNDED(7, "交易已退款"),
    //已关闭，交易在已过期状态下，系统自动关闭交易
    CLOSED(8, "交易已关闭");

    private int status;

    private String statusDesc;

    TransactionFlowStatusEnum(int status, String statusDesc) {
        this.status = status;
        this.statusDesc = statusDesc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }
}
