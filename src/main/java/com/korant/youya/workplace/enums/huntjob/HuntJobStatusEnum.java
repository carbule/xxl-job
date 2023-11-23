package com.korant.youya.workplace.enums.huntjob;

/**
 * @ClassName HuntJobStatusEnum
 * @Description
 * @Author chenyiqiang
 * @Date 2023/9/6 10:49
 * @Version 1.0
 */
public enum HuntJobStatusEnum {

    //未冻结
    UNPUBLISHED(0),
    //已冻结
    PUBLISHED(1);

    private int status;

    HuntJobStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
