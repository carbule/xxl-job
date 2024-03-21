package com.korant.youya.workplace.pojo.vo.expectedposition;

import lombok.Data;

/**
 * @ClassName PersonalExpectedPositionVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/7 14:56
 * @Version 1.0
 */
@Data
public class PersonalExpectedPositionVo {

    /**
     * 意向职位id
     */
    private Long positionId;

    /**
     * 行业名称
     */
    private String industryName;

    /**
     * 职业类型名称
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
     * 职业类型编码
     */
    private String typeCode;

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
     * 职业群名称
     */
    private String organizationLevel;

    /**
     * 职业等级名称
     */
    private String classLevel;
}
