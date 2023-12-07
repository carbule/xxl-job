package com.korant.youya.workplace.pojo.vo.huntjob;

import lombok.Data;

/**
 * @ClassName PersonalExpectedPositionVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/30 9:58
 * @Version 1.0
 */
@Data
public class PersonalExpectedPositionVo {

    /**
     * 主键
     */
    private Long id;

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
}
