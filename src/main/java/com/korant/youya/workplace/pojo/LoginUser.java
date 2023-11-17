package com.korant.youya.workplace.pojo;

import lombok.Data;

/**
 * @ClassName UserCache
 * @Description
 * @Author chenyiqiang
 * @Date 2023/9/6 15:31
 * @Version 1.0
 */
@Data
public class LoginUser {

    /**
     * id
     */
    private Long id;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 用户身份证号
     */
    private String identityCard;

    /**
     * 实名认证状态
     */
    private Integer authenticationStatus;

    /**
     * 账号状态 0-未冻结 1-已冻结
     */
    private Integer accountStatus;

    /**
     * 企业id
     */
    private Long enterpriseId;

    /**
     * 角色
     */
    private String role;
}
