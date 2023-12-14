package com.korant.youya.workplace.enums.enterprisechangetodo;

/**
 * @ClassName EnterpriseChangeTodoOperateEnum
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/13 10:52
 * @Version 1.0
 */
public enum EnterpriseChangeTodoOperateEnum {


    PENDING_REVIEW(0),
    REFUSE(1),
    AGREE(2);

    private int operate;

    EnterpriseChangeTodoOperateEnum(int operate) {
        this.operate = operate;
    }

    public int getOperate() {
        return operate;
    }

    public void setOperate(int operate) {
        this.operate = operate;
    }
}
