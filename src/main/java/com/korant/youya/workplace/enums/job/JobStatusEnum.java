package com.korant.youya.workplace.enums.job;

/**
 * @ClassName HuntJobStatusEnum
 * @Description
 * @Author chenyiqiang
 * @Date 2023/9/6 10:49
 * @Version 1.0
 */
public enum JobStatusEnum {

    //未发布
    UNPUBLISHED(0),
    //已发布
    PUBLISHED(1);

    private int status;

    JobStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
