package com.korant.youya.workplace.pojo.vo.expectedposition;

import lombok.Data;

/**
 * @ClassName ExpectedPositionVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/8 16:48
 * @Version 1.0
 */
@Data
public class ExpectedPositionVo {

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
     * 最低薪资
     */
    private Integer minSalary;

    /**
     * 最高薪资
     */
    private Integer maxSalary;
}
