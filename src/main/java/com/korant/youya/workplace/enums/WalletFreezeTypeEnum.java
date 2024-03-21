package com.korant.youya.workplace.enums;

/**
 * @ClassName WalletFreezeTypeEnum
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/21 15:08
 * @Version 1.0
 */
public enum WalletFreezeTypeEnum {

    //冻结
    FREEZE(1),
    //解冻
    UNFREEZE(2);

    private Integer type;

    WalletFreezeTypeEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
