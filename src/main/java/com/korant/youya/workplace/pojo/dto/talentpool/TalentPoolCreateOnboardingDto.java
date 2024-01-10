package com.korant.youya.workplace.pojo.dto.talentpool;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName TalentPoolCreateOnboardingDto
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/10 16:00
 * @Version 1.0
 */
@Data
public class TalentPoolCreateOnboardingDto {

    /**
     * 主键
     */
    @NotNull(message = "人才id不能为空")
    private Long id;

    /**
     * 入职时间
     */
    @NotNull(message = "入职时间不能为空")
    private LocalDateTime onboardingTime;

    /**
     * 国家编码
     */
    @NotBlank(message = "国家编码不能为空")
    private String countryCode;

    /**
     * 省份编码
     */
    @NotBlank(message = "省份编码不能为空")
    private String provinceCode;

    /**
     * 市级编码
     */
    @NotBlank(message = "市级编码不能为空")
    private String cityCode;

    /**
     * 入职地点
     */
    @NotBlank(message = "入职地点不能为空")
    private String address;

    /**
     * 备注
     */
    private String note;
}
