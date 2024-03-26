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
    //奖励
    REWARD(2),
    //退款
    REFUND(3),
    //提现
    WITHDRAWAL(4),
    //冻结或解冻
    FREEZE_OR_UNFREEZE(5),
    //转账
    TRANSFER(6);

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
