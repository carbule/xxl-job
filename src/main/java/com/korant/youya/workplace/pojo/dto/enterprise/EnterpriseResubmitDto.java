package com.korant.youya.workplace.pojo.dto.enterprise;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * @ClassName EnterpriseResubmitDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/15 16:08
 * @Version 1.0
 */
@Data
public class EnterpriseResubmitDto {

    /**
     * 企业id
     */
    @NotNull(message = "企业id不能为空")
    private Long id;

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
     * 成立日期
     */
    @NotNull(message = "成立日期")
    private LocalDate establishDate;

    /**
     * 法人
     */
    private String corporation;

    /**
     * 注册地址
     */
    @NotBlank(message = "注册地址不能为空")
    private String registerAddress;

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
