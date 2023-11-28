package com.korant.youya.workplace.pojo.vo.enterprisetodo;

import com.korant.youya.workplace.annotations.Dict;
import lombok.Data;

/**
 * @Date 2023/11/22 10:17
 * @ClassName: EnterpriseTodoDetailVo
 * @Description:
 * @Version 1.0
 */
@Data
public class EnterpriseTodoDetailVo {

    /**
     * 企业id
     */
    private Long id;

    /**
     * 企业名称
     */
    private String name;

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
     * 操作 0-待审核 1-拒绝 2-同意
     */
    private Integer operate;

    /**
     * 管理员姓氏
     */
    private String lastName;

    /**
     * 管理员名字
     */
    private String firstName;

    /**
     * 管理员头像
     */
    private String avatar;

}
