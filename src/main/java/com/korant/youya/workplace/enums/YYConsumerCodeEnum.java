package com.korant.youya.workplace.enums;

/**
 * @ClassName YYConsumerCodeEnum
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/27 10:15
 * @Version 1.0
 */
public enum YYConsumerCodeEnum {

    USER("500"),
    ENTERPRISE("600"),
    SYSTEM("700");

    private String code;

    YYConsumerCodeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
