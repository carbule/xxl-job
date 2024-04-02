package com.korant.youya.workplace.enums;

/**
 * @ClassName TaskStatusEnum
 * @Description
 * @Author chenyiqiang
 * @Date 2024/4/2 13:56
 * @Version 1.0
 */
public enum TaskStatusEnum {

    RUNNING(1),
    SUCCESS(2),
    FAIL(3);

    private int status;

    TaskStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
