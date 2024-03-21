package com.korant.youya.workplace.enums;

/**
 * @ClassName TransactionTypeEnum
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/6 11:12
 * @Version 1.0
 */
public enum TransactionTypeEnum {

    //充值
    RECHARGE(1),
    //订单
    ORDER(2),
    //退款
    REFUND(3),
    //提现
    WITHDRAWAL(4),
    //冻结
    FREEZE(5),
    //解冻
    UNFREEZE(6),
    //转账
    TRANSFER(7);

    private Integer type;

    TransactionTypeEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
