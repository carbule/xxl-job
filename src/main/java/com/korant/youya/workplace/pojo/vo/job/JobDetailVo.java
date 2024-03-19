package com.korant.youya.workplace.pojo.vo.job;

import com.korant.youya.workplace.annotations.Dict;
import com.korant.youya.workplace.pojo.vo.enterprisewelfarelabel.EnterpriseWelfareLabelDataVo;
import lombok.Data;

import java.util.List;

/**
 * @ClassName JobDetailVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/29 20:34
 * @Version 1.0
 */
@Data
public class JobDetailVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 行业名称
     */
    private String industryName;

    /**
     * 职业类型
     */
    private String typeName;

    /**
     * 领域名称
     */
    private String sectorName;

    /**
     * 职位名称
     */
    private String positionName;

    /**
     * 行业编码
     */
    private String industryCode;

    /**
     * 领域编码
     */
    private String sectorCode;

    /**
     * 职位编码
     */
    private String positionCode;

    /**
     * 学历要求
     */
    @Dict(categoryCode = "education")
    private Integer eduRequirements;

    /**
     * 工作经验
     */
    @Dict(categoryCode = "work_experience")
    private Integer workExperience;

    /**
     * 最低薪资
     */
    private Integer minSalary;

    /**
     * 最高薪资
     */
    private Integer maxSalary;

    /**
     * 职位描述
     */
    private String jobDesc;

    /**
     * 其他说明项
     */
    private String otherInfo;

    /**
     * 国家名称
     */
    private String countryName;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 市级名称
     */
    private String cityName;

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
     * 详细地址
     */
    private String detailAddress;

    /**
     * 推荐奖励
     */
    private Integer award;

    /**
     * 面试奖励分配比例
     */
    private Integer interviewRewardRate;

    /**
     * 入职奖励分配比例
     */
    private Integer onboardRewardRate;

    /**
     * 转正奖励分配比例
     */
    private Integer fullMemberRewardRate;

    /**
     * 企业福利标签集
     */
    private List<EnterpriseWelfareLabelDataVo> welfareLabelDataVoList;
}
