package com.korant.youya.workplace.enums;

/**
 * @ClassName OrderTypeEnum
 * @Description
 * @Author chenyiqiang
 * @Date 2024/2/29 16:25
 * @Version 1.0
 */
public enum OrderTypeEnum {

    //普通订单
    NORMAL(1),
    //虚拟商品订单
    VIRTUAL_PRODUCT(2),
    //订阅订单
    SUBSCRIPTION(3),
    //线下订单
    OFFLINE(4);

    private Integer type;

    OrderTypeEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
