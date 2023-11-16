package com.korant.youya.workplace.pojo.vo.intendedposition;

import lombok.Data;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/16 12:08
 * @PackageName:com.korant.youya.workplace.pojo.dto.intendedposition
 * @ClassName: IntendedPositionInfoDto
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class IntendedPositionInfoVo {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 行业id
     */
    private Long industryId;

    /**
     * 行业名称
     */
    private String industryName;

    /**
     * 领域id
     */
    private Long sectorId;

    /**
     * 领域名称
     */
    private String sectorName;

    /**
     * 职位id
     */
    private Long positionId;

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
