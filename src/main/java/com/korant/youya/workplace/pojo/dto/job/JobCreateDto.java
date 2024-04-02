package com.korant.youya.workplace.pojo.dto.job;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName JobCreateDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/29 16:12
 * @Version 1.0
 */
@Data
public class JobCreateDto {

    /**
     * 行业编码
     */
    @NotBlank(message = "行业不能为空")
    private String industryCode;

    /**
     * 职业类型不能为空
     */
    @NotBlank(message = "职业类型不能为空")
    private String typeCode;

    /**
     * 领域编码
     */
    @NotBlank(message = "领域不能为空")
    private String sectorCode;

    /**
     * 职位编码
     */
    @NotBlank(message = "职位不能为空")
    private String positionCode;

    /**
     * 职位名称
     */
    @NotBlank(message = "职位名称不能为空")
    private String positionName;

    /**
     * 学历要求
     */
    @NotNull(message = "学历不能为空")
    private Integer eduRequirements;

    /**
     * 工作经验
     */
    @NotNull(message = "工作经验不能为空")
    private Integer workExperience;

    /**
     * 最低薪资
     */
    @NotNull(message = "最低薪资不能为空")
    private Integer minSalary;

    /**
     * 最高薪资
     */
    @NotNull(message = "最高薪资不能为空")
    private Integer maxSalary;

    /**
     * 职位描述
     */
    @Size(max = 2000, message = "最多不得超过1000字")
    private String jobDesc;

    /**
     * 其他说明项
     */
    @Size(max = 500, message = "最多不得超过500字")
    private String otherInfo;

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
     * 详细地址
     */
    @NotBlank(message = "详细地址不能为空")
    private String detailAddress;

    /**
     * 推荐奖励
     */
    private String award;

    /**
     * 面试奖励分配比例
     */
    private BigDecimal interviewRewardRate;

    /**
     * 入职奖励分配比例
     */
    private BigDecimal onboardRewardRate;

    /**
     * 转正奖励分配比例
     */
    private BigDecimal fullMemberRewardRate;

    /**
     * 福利标签集合
     */
    private List<Long> welfareLabelIdList;
}
