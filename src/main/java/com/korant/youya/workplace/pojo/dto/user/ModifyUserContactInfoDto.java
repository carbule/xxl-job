package com.korant.youya.workplace.pojo.dto.user;

import jakarta.validation.constraints.Email;
import lombok.Data;

/**
 * @ClassName ModifyUserContactInfoDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/6 14:48
 * @Version 1.0
 */
@Data
public class ModifyUserContactInfoDto {

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
    @Email(message = "邮箱格式错误")
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
