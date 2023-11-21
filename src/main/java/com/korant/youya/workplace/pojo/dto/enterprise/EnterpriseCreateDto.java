package com.korant.youya.workplace.pojo.dto.enterprise;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/20 13:52
 * @PackageName:com.korant.youya.workplace.pojo.dto.enterprise
 * @ClassName: EnterpriseCreateDto
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class EnterpriseCreateDto {

    /**
     * 企业名称
     */
    @NotBlank(message = "企业名称不能为空")
    private String name;

    /**
     * 社会信用代码
     */
    @NotBlank(message = "社会信用代码不能为空")
    private String socialCreditCode;

    /**
     * 注册地址
     */
    @NotBlank(message = "注册地址不能为空")
    private String registerAddress;

    /**
     * 成立日期
     */
    @NotBlank(message = "成立日期不能为空")
    private LocalDate establishDate;

    /**
     * 授权书
     */
    @NotBlank(message = "授权书不能为空")
    private String powerOfAttorney;

    /**
     * 营业执照
     */
    @NotBlank(message = "营业执照不能为空")
    private String businessLicense;

}
