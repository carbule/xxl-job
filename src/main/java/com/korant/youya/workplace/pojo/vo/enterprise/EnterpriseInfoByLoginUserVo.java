package com.korant.youya.workplace.pojo.vo.enterprise;

import lombok.Data;

/**
 * @ClassName EnterpriseInfoByLoginUserVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/13 16:13
 * @Version 1.0
 */
@Data
public class EnterpriseInfoByLoginUserVo {

    /**
     * 企业id
     */
    private Long id;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 企业logo
     */
    private String logo;

    /**
     * 企业类型
     */
    private Integer entType;

    /**
     * 企业规模
     */
    private Integer scale;

    /**
     * 融资阶段
     */
    private Integer financingStage;

    /**
     * 认证状态
     */
    private Integer authStatus;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户姓氏
     */
    private String lastName;

    /**
     * 用户名字
     */
    private String firstName;

    /**
     * 审核状态
     */
    private Integer operate;
}
