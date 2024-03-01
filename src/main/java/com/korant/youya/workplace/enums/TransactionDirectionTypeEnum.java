package com.korant.youya.workplace.enums;

/**
 * @ClassName TransactionDirectionTypeEnum
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/1 10:17
 * @Version 1.0
 */
public enum TransactionDirectionTypeEnum {

    //入账
    CREDIT(1),
    //出账
    DEBIT(2);

    private Integer type;

    TransactionDirectionTypeEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
