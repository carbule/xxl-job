package com.korant.youya.workplace.pojo.vo.expectedposition;

import lombok.Data;

/**
 * @Date 2023/11/17 14:31
 * @PackageName:com.korant.youya.workplace.pojo.vo.expectedposition
 * @ClassName: ExpectedPositionInfoVo
 * @Description:
 * @Version 1.0
 */
@Data
public class ExpectedPositionInfoVo {

    /**
     * id
     */
    private Long id;

    /**
     * 行业编码
     */
    private Long industryCode;

    /**
     * 领域编码
     */
    private Long sectorCode;

    /**
     * 职位编码
     */
    private Long positionCode;

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
