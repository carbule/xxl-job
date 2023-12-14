package com.korant.youya.workplace.pojo.vo.enterprise;

import com.korant.youya.workplace.annotations.Dict;
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
    @Dict(categoryCode = "enterprise_type")
    private Integer entType;

    /**
     * 企业规模
     */
    @Dict(categoryCode = "enterprise_scale")
    private Integer scale;

    /**
     * 融资阶段
     */
    @Dict(categoryCode = "financing_stage")
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

    /**
     * 角色信息
     */
    private String role;
}
