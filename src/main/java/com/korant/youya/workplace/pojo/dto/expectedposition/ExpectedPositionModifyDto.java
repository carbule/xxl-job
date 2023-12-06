package com.korant.youya.workplace.pojo.dto.expectedposition;

import lombok.Data;

/**
 * @ClassName ExpectedPositionModifyDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/6 17:42
 * @Version 1.0
 */
@Data
public class ExpectedPositionModifyDto {

    /**
     * 主键
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
     * 最低薪资
     */
    private Integer minSalary;

    /**
     * 最高薪资
     */
    private Integer maxSalary;
}
