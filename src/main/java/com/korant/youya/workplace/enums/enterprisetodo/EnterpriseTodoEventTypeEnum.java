package com.korant.youya.workplace.enums.enterprisetodo;

/**
 * @ClassName EnterpriseTodoEventTypeEnum
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/13 10:49
 * @Version 1.0
 */
public enum EnterpriseTodoEventTypeEnum {

    HR(1),
    EMPLOYEE(2);

    private int type;

    EnterpriseTodoEventTypeEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
