package com.korant.youya.workplace.enums;

/**
 * @ClassName MainTaskTypeEnum
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/29 17:28
 * @Version 1.0
 */
public enum MainTaskTypeEnum {

    CANDIDATE(1),
    TALENT_POOL(2);

    private int type;

    MainTaskTypeEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
