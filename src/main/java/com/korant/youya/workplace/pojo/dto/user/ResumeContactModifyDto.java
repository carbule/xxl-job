package com.korant.youya.workplace.pojo.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Date 2023/11/21 16:23
 * @PackageName:com.korant.youya.workplace.pojo.dto.user
 * @ClassName: ResumeContactModifyDto
 * @Description:
 * @Version 1.0
 */
@Data
public class ResumeContactModifyDto {

    /**
     * 用户手机号
     */
    @NotNull(message = "手机号不能为空")
    private String phone;

    /**
     * 用户微信号
     */
    private String wechatId;

    /**
     * 用户QQ号
     */
    private String qq;

    /**
     * 用户邮箱
     */
    @Email
    private String email;

    /**
     * 国家编码
     */
    private String countryCode;

    /**
     * 省份编码
     */
    private String provinceCode;

    /**
     * 市级编码
     */
    private String cityCode;

    /**
     * 行政区编码
     */
    private String districtCode;

    /**
     *  详细地址
     */
    private String address;

}
