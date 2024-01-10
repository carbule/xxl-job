package com.korant.youya.workplace.enums;

/**
 * @ClassName CompletionStatusEnum
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/10 11:15
 * @Version 1.0
 */
public enum CompletionStatusEnum {

    //未完成
    INCOMPLETE(0),
    //已取消
    CANCELED(1),
    //完成
    COMPLETE(2);

    private int status;

    CompletionStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
