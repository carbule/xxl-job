package com.korant.youya.workplace.enums.enterprisechangetodo;

/**
 * @ClassName EnterpriseChangeTodoTypeEnum
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/13 10:49
 * @Version 1.0
 */
public enum EnterpriseChangeTodoTypeEnum {

    NAME_CHANGE(1),
    ADDRESS_CHANGE(2);

    private int type;

    EnterpriseChangeTodoTypeEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
