package com.korant.youya.workplace.enums;

/**
 * @ClassName CurrencyTypeEnum
 * @Description
 * @Author chenyiqiang
 * @Date 2024/2/29 16:15
 * @Version 1.0
 */
public enum CurrencyTypeEnum {

    //人民币
    CNY("CNY"),
    //美元
    USD("USD"),
    //欧元
    EUR("EUR"),
    //英镑
    GBP("GBP");

    private String type;

    CurrencyTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
