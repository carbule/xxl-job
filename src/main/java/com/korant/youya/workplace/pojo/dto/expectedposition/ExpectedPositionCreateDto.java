package com.korant.youya.workplace.pojo.dto.expectedposition;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/17 14:28
 * @PackageName:com.korant.youya.workplace.pojo.dto.expectedposition
 * @ClassName: ExpectedPositionCreateDto
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class ExpectedPositionCreateDto {

    /**
     * 状态id
     */
    @NotNull(message = "状态id不能为空")
    private Long statusId;

    /**
     * 行业id
     */
    @NotNull(message = "行业id不能为空")
    private Long industryId;

    /**
     * 领域id
     */
    @NotNull(message = "领域id不能为空")
    private Long sectorId;

    /**
     * 职位id
     */
    @NotNull(message = "职位id不能为空")
    private Long positionId;

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
