package com.korant.youya.workplace.enums;

/**
 * @ClassName YYBusinessCode
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/27 10:19
 * @Version 1.0
 */
public enum YYBusinessCode {

    /**
     * 用户业务码
     */
    //用户钱包账户充值
    USER_RECHARGE("11000"),
    //用户钱包账户提现
    USER_WITHDRAWAL("12000"),
    //用户钱包账户冻结或解冻
    USER_FREEZE_OR_UNFREEZE("13000"),
    //用户求职入职奖金
    USER_ONBOARD_REWARD("14002"),

    //--------------------------------------

    /**
     * 企业业务码
     */
    //企业充值
    ENTERPRISE_RECHARGE("21000"),
    //企业钱包账户冻结或解冻
    ENTERPRISE_FREEZE_OR_UNFREEZE("23000"),
    //企业面试奖金
    ENTERPRISE_INTERVIEW_REWARD("24001"),
    //企业入职奖金
    ENTERPRISE_ONBOARD_REWARD("24002"),
    //企业转正奖金
    ENTERPRISE_FULL_MEMBER_REWARD("24003"),


    //--------------------------------------

    /**
     * 系统业务码
     */
    //平台奖金抽成
    SYSTEM_COMMISSION("31000");

    private String code;

    YYBusinessCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
