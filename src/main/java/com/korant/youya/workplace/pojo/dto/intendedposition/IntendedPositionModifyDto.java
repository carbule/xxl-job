package com.korant.youya.workplace.pojo.dto.intendedposition;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/16 14:29
 * @PackageName:com.korant.youya.workplace.pojo.dto.intendedposition
 * @ClassName: IntendedPositionModifyDto
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class IntendedPositionModifyDto {

    /**
     * id
     */
    @NotNull(message = "id不能为空")
    private Long id;

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
