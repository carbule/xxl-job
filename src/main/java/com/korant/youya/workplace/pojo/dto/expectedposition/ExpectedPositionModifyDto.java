package com.korant.youya.workplace.pojo.dto.expectedposition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private Long positionId;

    /**
     * 意向id
     */
    private Long statusId;

    /**
     * 行业编码
     */
    @NotBlank(message = "行业编码不能为空")
    private String industryCode;

    /**
     * 职业类型编码
     */
    @NotBlank(message = "职业类型编码")
    private String typeCode;

    /**
     * 领域编码
     */
    @NotBlank(message = "领域编码不能为空")
    private String sectorCode;

    /**
     * 职位编码
     */
    @NotBlank(message = "职位编码不能为空")
    private String positionCode;

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
}
