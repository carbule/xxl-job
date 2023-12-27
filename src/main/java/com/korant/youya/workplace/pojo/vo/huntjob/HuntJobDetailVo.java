package com.korant.youya.workplace.pojo.vo.huntjob;

import lombok.Data;

/**
 * @ClassName HuntJobDetailVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/30 16:21
 * @Version 1.0
 */
@Data
public class HuntJobDetailVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 意向职位id
     */
    private Long positionId;

    /**
     * 行业名称
     */
    private String industryName;

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
     * 最低薪资
     */
    private Integer minSalary;

    /**
     * 最高薪资
     */
    private Integer maxSalary;

    /**
     * 期望区域id
     */
    private Long areaId;

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
}
