package com.korant.youya.workplace.pojo.vo.jobqrcode;

import com.korant.youya.workplace.annotations.Dict;
import com.korant.youya.workplace.pojo.vo.enterprisewelfarelabel.EnterpriseWelfareLabelDataVo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName JobQrCodeDetailVo
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/15 15:59
 * @Version 1.0
 */
@Data
public class JobQrCodeDetailVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 企业id
     */
    private Long enterpriseId;

    /**
     * 企业logo
     */
    private String logo;

    /**
     * 企业名称
     */
    private String enterpriseName;

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
     * 职位名称
     */
    private String positionName;

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
     * 详细地址
     */
    private String detailAddress;

    /**
     * 经度
     */
    private Float longitude;

    /**
     * 纬度
     */
    private Float latitude;

    /**
     * 推荐奖励
     */
    private Integer award;

    /**
     * 上次分享时间
     */
    private LocalDateTime lastShareTime;

    /**
     * 分享总次数
     */
    private Integer shareTotal;

    /**
     * 企业福利标签集
     */
    private List<EnterpriseWelfareLabelDataVo> welfareLabelDataVoList;
}