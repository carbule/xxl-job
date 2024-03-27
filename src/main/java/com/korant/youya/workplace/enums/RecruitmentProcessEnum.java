package com.korant.youya.workplace.enums;

/**
 * @ClassName RecruitmentProcessEnum
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/27 20:34
 * @Version 1.0
 */
public enum RecruitmentProcessEnum {

    //面试
    INTERVIEW(1),
    //入职
    ONBOARD(2),
    //转正
    FULL_MEMBER(3);

    private int type;

    RecruitmentProcessEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
