package com.korant.youya.workplace.pojo.dto.enterprise;

import com.baomidou.mybatisplus.annotation.TableField;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/20 13:52
 * @PackageName:com.korant.youya.workplace.pojo.dto.enterprise
 * @ClassName: EnterpriseModifyDto
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class EnterpriseModifyDto {

    /**
     * 企业id
     */
    @NotNull(message = "企业id不能为空")
    private Long id;

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
     * 企业网址
     */
    private String website;

    /**
     * 企业简介
     */
    private String introduction;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式错误")
    private String email;

    /**
     * 国家id
     */
    private Long countryId;

    /**
     * 省份id
     */
    private Long provinceId;

    /**
     * 市级id
     */
    private Long cityId;

    /**
     * 行政区id
     */
    private Long districtId;

    /**
     * 联系地址
     */
    private String contactAddress;

}
